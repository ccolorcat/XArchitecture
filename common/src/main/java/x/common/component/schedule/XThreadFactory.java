package x.common.component.schedule;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-28
 * GitHub: https://github.com/ccolorcat
 */
final class XThreadFactory implements ThreadFactory {
    private final AtomicLong count = new AtomicLong(0L);
    private final String prefix;
    private final int priority;


    XThreadFactory(@NonNull String prefix) {
        this(prefix, Thread.NORM_PRIORITY);
    }

    XThreadFactory(@NonNull String prefix, int priority) {
        this.prefix = Utils.requireNonNull(prefix, "prefix == null") + '-';
        this.priority = Math.max(priority, Thread.MIN_PRIORITY);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, prefix + count.getAndIncrement());
        t.setPriority(priority);
        return t;
    }
}
