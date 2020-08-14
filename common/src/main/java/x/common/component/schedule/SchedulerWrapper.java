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
    public void remove(@NonNull Runnable task) {
        executor.remove(task);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        executor.execute(command);
    }

    @Override
    public <V> Future<V> submit(@NonNull Callable<V> task) {
        return executor.submit(task);
    }

    @Override
    public <V> Future<V> submit(@NonNull Runnable task, V result) {
        return executor.submit(task, result);
    }

    @Override
    public Future<?> submit(@NonNull Runnable task) {
        return executor.submit(task);
    }

    @NonNull
    @Override
    public Future<?> schedule(@NonNull Runnable command, long delay, @NonNull TimeUnit unit) {
        return executor.schedule(command, delay, unit);
    }

    @NonNull
    @Override
    public <V> Future<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit) {
        return executor.schedule(callable, delay, unit);
    }

    @NonNull
    @Override
    public Future<?> scheduleWithFixedDelay(@NonNull Runnable command, long initialDelay, long delay, @NonNull TimeUnit unit) {
        return executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}
