package x.common.component.schedule;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public class SchedulerWrapper implements XScheduler {
    private final ScheduledThreadPoolExecutor executor;

    protected SchedulerWrapper(int corePoolSize, @NonNull ThreadFactory factory) {
        executor = new ScheduledThreadPoolExecutor(corePoolSize, factory);
    }

    protected SchedulerWrapper(int corePoolSize) {
        executor = new ScheduledThreadPoolExecutor(corePoolSize, new XThreadFactory("wrapper-x-thread"));
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public void executeDelay(@NonNull Runnable runnable, long delay, @NonNull TimeUnit unit) {
        executor.schedule(runnable, delay, unit);
    }

    @Override
    public <V> Future<V> submit(@NonNull Callable<V> callable) {
        return executor.submit(callable);
    }

    @Override
    public <V> Future<V> submit(@NonNull Runnable runnable, V result) {
        return executor.submit(runnable, result);
    }

    @Override
    public Future<?> submit(@NonNull Runnable runnable) {
        return executor.submit(runnable);
    }

    @NonNull
    @Override
    public Future<?> schedule(@NonNull Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return executor.schedule(runnable, delay, unit);
    }

    @NonNull
    @Override
    public <V> Future<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit) {
        return executor.schedule(callable, delay, unit);
    }

    @NonNull
    @Override
    public Future<?> scheduleWithFixedDelay(@NonNull Runnable runnable, long initialDelay, long delay, @NonNull TimeUnit unit) {
        return executor.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
    }
}
