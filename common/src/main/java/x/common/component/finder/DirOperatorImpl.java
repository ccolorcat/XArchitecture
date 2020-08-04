package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.IOException;

import x.common.component.XLruCache;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-13
 */
final class DirOperatorImpl implements DirOperator {
    private final DiskCache diskCache;
    private final XLruCache<String, FileOperator> fileOperators = new XLruCache<>(8);

    DirOperatorImpl(@NonNull DiskCache diskCache) {
        this.diskCache = Utils.requireNonNull(diskCache, "diskCache == null");
    }

    @NonNull
    @Override
    public FileOperator getFileOperator(String filename) {
        return getFileOperator(Filename.from(filename));
    }

    @NonNull
    @Override
    public FileOperator getFileOperator(@NonNull Filename filename) {
        return getFileOperator(filename.name, filename.type);
    }

    @NonNull
    @Override
    public FileOperator getFileOperator(@NonNull String name, @NonNull String type) {
        return realGetFileOperator(createKey(name, type));
    }

    @NonNull
    private FileOperator realGetFileOperator(@NonNull String key) {
        FileOperator fo = fileOperators.get(key);
        if (fo == null) {
            fo = new FileOperatorImpl(diskCache.getSnapshot(key));
            fileOperators.put(key, fo);
        }
        return fo;
    }

    @Override
    public long maxSize() {
        return diskCache.maxSize();
    }

    @Override
    public boolean exists() {
        return diskCache.directory.exists();
    }

    @Override
    public long size() {
        return diskCache.size();
    }

    @Override
    public void delete() {
        try {
            diskCache.clear();
        } catch (IOException ignore) {
        }
    }

    @Override
    public String toString() {
        return "DirOperatorImpl{" +
                "directory=" + diskCache.directory +
                '}';
    }


    private static String createKey(String name, String type) {
        Utils.requireNonNull(name, "name == null");
        Utils.requireNonNull(type, "type == null");
        return (Utils.md5(name) + '.' + type).toLowerCase();
    }
}
