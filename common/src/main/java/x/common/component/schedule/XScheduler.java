package x.common.component.schedule;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public interface XScheduler {
    void shutdown();

    boolean isShutdown();

    void execute(@NonNull Runnable runnable);

    void executeDelay(@NonNull Runnable runnable, long delay, @NonNull TimeUnit unit);

    default <V> Future<V> submit(@NonNull Callable<V> callable) {
        return schedule(callable, 0L, TimeUnit.MILLISECONDS);
    }

    default <V> Future<V> submit(@NonNull Runnable runnable, V result) {
        Callable<V> callable = Executors.callable(runnable, result);
        return schedule(callable, 0L, TimeUnit.MILLISECONDS);
    }

    default Future<?> submit(@NonNull Runnable runnable) {
        return schedule(runnable, 0L, TimeUnit.MILLISECONDS);
    }

    @NonNull
    Future<?> schedule(@NonNull Runnable runnable, long delay, @NonNull TimeUnit unit);

    @NonNull
    <V> Future<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit);

    @NonNull
    Future<?> scheduleWithFixedDelay(@NonNull Runnable runnable, long initialDelay, long delay, @NonNull TimeUnit unit);
}
