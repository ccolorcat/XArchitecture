package x.common.component.finder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import x.common.component.XLruCache;
import x.common.component.log.Logger;
import x.common.component.schedule.XScheduler;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-14
 */
final class Finder {
//    private static final long MAX_SIZE_PER_DIR = Long.MAX_VALUE;

    private final XLruCache<File, DirOperator> dirOperators = new XLruCache<>(8);
    private final XScheduler scheduler;
    final File rootDir;
    private final long maxSizePerDir;

    Finder(@NonNull XScheduler scheduler, @NonNull File rootDir, long maxSizePerDir) {
        this.scheduler = Utils.requireNonNull(scheduler, "scheduler == null");
        this.rootDir = Utils.requireNonNull(rootDir, "rootDir == null");
        this.maxSizePerDir = maxSizePerDir;
    }

    @Nullable
    DirOperator getDirOperator(String... subpath) {
        File directory = rootDir;
        if (subpath != null && subpath.length > 0) {
            directory = getPath(directory, subpath);
        }
        return realGetDirOperator(directory);
    }

    @Nullable
    private DirOperator realGetDirOperator(File directory) {
        DirOperator operator = dirOperators.get(directory);
        if (operator == null) {
            operator = createDirOperator(directory);
            if (operator != null) {
                dirOperators.put(directory, operator);
            }
        }
        return operator;
    }

    long size() {
        return rootDir.length();
    }

    void delete() {
        try {
            //noinspection ResultOfMethodCallIgnored
            rootDir.delete();
        } catch (Throwable e) {
            Logger.getDefault().e(e);
        }
    }

    @Nullable
    private DirOperator createDirOperator(@NonNull File directory) {
        try {
            DiskCache cache = DiskCache.open(scheduler, directory, maxSizePerDir);
            return new DirOperatorImpl(cache);
        } catch (IOException e) {
            Logger.getDefault().e(e);
        }
        return null;
    }

    @NonNull
    static File getPath(@NonNull File dir, String... more) {
        if (more == null) return dir;
        StringBuilder builder = new StringBuilder(dir.getAbsolutePath());
        for (String path : more) {
            builder.append(File.separator).append(Utils.requireNonNull(path));
        }
        return new File(builder.toString());
    }
}
