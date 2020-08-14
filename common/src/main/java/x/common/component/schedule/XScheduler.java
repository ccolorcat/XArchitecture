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
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface XScheduler {
    void shutdown();

    boolean isShutdown();

    void remove(@NonNull Runnable task);

    void execute(@NonNull Runnable command);

    default <V> Future<V> submit(@NonNull Callable<V> task) {
        return schedule(task, 0L, TimeUnit.MILLISECONDS);
    }

    default <V> Future<V> submit(@NonNull Runnable task, V result) {
        Callable<V> callable = Executors.callable(task, result);
        return schedule(callable, 0L, TimeUnit.MILLISECONDS);
    }

    default Future<?> submit(@NonNull Runnable task) {
        return schedule(task, 0L, TimeUnit.MILLISECONDS);
    }

    @NonNull
    Future<?> schedule(@NonNull Runnable command, long delay, @NonNull TimeUnit unit);

    @NonNull
    <V> Future<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit);

    @NonNull
    Future<?> scheduleWithFixedDelay(@NonNull Runnable command, long initialDelay, long delay, @NonNull TimeUnit unit);
}
