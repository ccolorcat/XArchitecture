package x.common.component.finder;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import x.common.component.log.Logger;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-13
 */
final class DiskCache {
    private static final String DIRTY_SUFFIX = ".tmp";
    private static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[.a-z0-9_-]{1,64}");

    static DiskCache open(@NonNull ThreadPoolExecutor executor, @NonNull File directory, long maxSize) throws IOException {
        Utils.requireNonNull(executor, "executor == null");
        if (maxSize <= 0L) throw new IllegalArgumentException("maxSize <= 0L");
        if (directory.isFile()) throw new IOException(directory + " is not a directory");
        synchronized (DiskCache.class) {
            if (directory.exists() || directory.mkdirs()) {
                DiskCache cache = new DiskCache(executor, directory, maxSize);
                cache.cleanDirtyFile();
                cache.readSnapshots();
                cache.asyncTrimToSize();
                return cache;
            }
            throw new IOException("failed to create directory: " + directory);
        }
    }

    private final LinkedHashMap<String, Snapshot> map;
    final File directory;

    private final long maxSize;
    private long size;
    private final ThreadPoolExecutor executor;

    private final Callable<Void> cleanupCallable = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            synchronized (DiskCache.this) {
                trimToSize(maxSize);
                return null;
            }
        }
    };

    private DiskCache(@NonNull ThreadPoolExecutor executor, @NonNull File directory, long maxSize) {
        this.executor = executor;
        this.directory = directory;
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<>(0, 0.75F, true);
    }

    private void cleanDirtyFile() throws IOException {
        File[] dirty = directory.listFiles(file -> file.isFile() && file.getName().endsWith(DIRTY_SUFFIX));
        assert dirty != null;
        deleteIfExists(dirty);
    }

    private void readSnapshots() throws IOException {
        File[] files = directory.listFiles(File::isFile);
        assert files != null;
        List<File> list = Arrays.asList(files);
        Collections.sort(list, new FileComparator());
        for (int i = 0, size = list.size(); i < size; ++i) {
            File file = list.get(i);
            this.size += file.length();
            String name = file.getName();
            map.put(name, new Snapshot(name));
        }
    }

    synchronized Snapshot getSnapshot(String key) {
        checkKey(key);
        Snapshot snapshot = map.get(key);
        if (snapshot == null) {
            snapshot = new Snapshot(key);
            map.put(key, snapshot);
        }
        return snapshot;
    }

    void clear() throws IOException {
        deleteContents(directory);
    }

    long maxSize() {
        return maxSize;
    }

    long size() {
        return size;
    }

    private static void checkKey(String key) {
        Matcher matcher = LEGAL_KEY_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + key + "\"");
        }
    }

    private void completeWriteSnapshot(Snapshot snapshot, boolean success) throws IOException {
        try {
            File dirty = snapshot.getDirtyFile();
            File clean = snapshot.getCleanFile();
            if (success) {
                if (dirty.exists()) {
                    long oldLength = clean.length();
                    long newLength = dirty.length();
                    renameTo(dirty, clean, true);
                    size = size - oldLength + newLength;
//                    asyncTrimToSize();
                }
            } else {
                deleteIfExists(dirty);
            }
        } finally {
            snapshot.writing = false;
            snapshot.committed = false;
            snapshot.hasErrors = false;
            if (snapshot.requiredDelete) {
                deleteSnapshot(snapshot);
            }
            asyncTrimToSize();
        }
    }

    private void deleteSnapshot(Snapshot snapshot) throws IOException {
        File clean = snapshot.getCleanFile();
        if (clean.exists()) {
            long length = clean.length();
            deleteIfExists(clean);
            if (map.remove(snapshot.key) != null) {
                size -= length;
            }
        }
    }

    private void asyncTrimToSize() {
        if (size > maxSize) {
            executor.submit(cleanupCallable);
        }
    }

    private void trimToSize(long maxSize) throws IOException {
        Iterator<Map.Entry<String, Snapshot>> iterator = map.entrySet().iterator();
        while (size > maxSize && iterator.hasNext()) {
            Map.Entry<String, Snapshot> toEvict = iterator.next();
            Snapshot value = toEvict.getValue();
            if (value.readCount == 0 && !value.writing) {
                File clean = value.getCleanFile();
                long cleanLength = clean.length();
                deleteIfExists(clean);
                size -= cleanLength;
                iterator.remove();
            }
        }
    }


    final class Snapshot {
        private String key;

        private int readCount = 0;

        private boolean writing = false;
        private boolean committed = false;
        private boolean hasErrors = false;

        private boolean requiredDelete = false;

        private Snapshot(String key) {
            this.key = key;
        }

        Uri getUri() {
            return Uri.fromFile(getCleanFile());
        }

        InputStream getInputStream() {
            synchronized (DiskCache.this) {
                try {
                    ++readCount;
                    return new SnapshotInputStream(new FileInputStream(getCleanFile()));
                } catch (FileNotFoundException e) {
                    --readCount;
                    return null;
                }
            }
        }

        InputStream newInputStream() throws FileNotFoundException {
            synchronized (DiskCache.this) {
                try {
                    ++readCount;
                    return new SnapshotInputStream(new FileInputStream(getCleanFile()));
                } catch (FileNotFoundException e) {
                    --readCount;
                    throw e;
                }
            }
        }

        OutputStream getOutputStream() {
            synchronized (DiskCache.this) {
                if (!writing) {
                    try {
                        FileOutputStream fos = new FileOutputStream(getDirtyFile());
                        writing = true;
                        return new SnapshotOutputStream(fos);
                    } catch (FileNotFoundException e) {
                        writing = false;
                        throw new IllegalStateException(directory + " does not exist.");
                    }
                }
                return null;
            }
        }

        OutputStream newOutputStream() throws IOException {
            synchronized (DiskCache.this) {
                File dirtyFile = getDirtyFile();
                if (!writing) {
                    try {
                        FileOutputStream fos = new FileOutputStream(dirtyFile);
                        writing = true;
                        return new SnapshotOutputStream(fos);
                    } catch (FileNotFoundException e) {
                        writing = false;
                        throw e;
                    }
                }
                throw new IOException(dirtyFile + " writing...");
            }
        }

        void requireDelete() throws IOException {
            synchronized (DiskCache.this) {
                if (!requiredDelete) {
                    requiredDelete = true;
                    if (readCount == 0 && !writing) {
                        deleteSnapshot(this);
                    }
                }
            }
        }

        long getContentLength() {
            return getCleanFile().length();
        }

        long getLastModified() {
            return getCleanFile().lastModified();
        }

        private void completeRead() throws IOException {
            synchronized (DiskCache.this) {
                --readCount;
                if (readCount < 0) {
                    throw new IllegalStateException("readCount < 0");
                }
                if (readCount == 0) {
                    if (writing) {
                        if (committed) {
                            completeWriteSnapshot(this, !hasErrors);
                        }
                    } else {
                        if (requiredDelete) {
                            deleteSnapshot(this);
                        }
                    }
                }
            }
        }

        private void commitWrite() throws IOException {
            synchronized (DiskCache.this) {
                if (writing && !committed) {
                    committed = true;
                    if (readCount == 0) {
                        completeWriteSnapshot(this, !hasErrors);
                    }
                } else {
                    throw new IllegalStateException("writing = " + writing + ", committed = " + committed);
                }
            }
        }

        private File getCleanFile() {
            return new File(directory, key);
        }

        private File getDirtyFile() {
            return new File(directory, key + DIRTY_SUFFIX);
        }

        @Override
        public String toString() {
            return "Snapshot{" +
                    "key='" + key + '\'' +
                    ", readCount=" + readCount +
                    ", writing=" + writing +
                    ", committed=" + committed +
                    ", hasErrors=" + hasErrors +
                    ", requiredDelete=" + requiredDelete +
                    '}';
        }

        private class SnapshotInputStream extends FilterInputStream {
            private boolean closed = false;

            private SnapshotInputStream(InputStream in) {
                super(in);
            }

            @Override
            public void close() throws IOException {
                if (!closed) {
                    closed = true;
                    try {
                        in.close();
                    } finally {
                        completeRead();
                    }
                }
            }
        }


        private class SnapshotOutputStream extends FilterOutputStream {
            private boolean closed = false;

            private SnapshotOutputStream(OutputStream out) {
                super(out);
            }

            @Override
            public void write(int oneByte) {
                try {
                    out.write(oneByte);
                } catch (IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void write(byte[] buffer) {
                write(buffer, 0, buffer.length);
            }

            @Override
            public void write(byte[] buffer, int offset, int length) {
                try {
                    out.write(buffer, offset, length);
                } catch (IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void flush() {
                try {
                    out.flush();
                } catch (IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void close() {
                if (!closed) {
                    closed = true;
                    try {
                        out.close();
                    } catch (IOException e) {
                        hasErrors = true;
                    } finally {
                        try {
                            commitWrite();
                        } catch (IOException e) {
                            Logger.getDefault().e(e);
                        }
                    }
                }
            }
        }
    }

    private static class FileComparator implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return Long.compare(f1.lastModified(), f2.lastModified());
        }
    }

    private static void deleteIfExists(File... files) throws IOException {
        for (File file : files) {
            deleteIfExists(file);
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException("failed to delete file: " + file);
        }
    }

    private static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) throw new IOException("not a readable directory: " + dir);
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    private static void renameTo(File from, File to, boolean deleteDest) throws IOException {
        if (deleteDest) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException("failed to rename from " + from + " to " + to);
        }
    }
}
