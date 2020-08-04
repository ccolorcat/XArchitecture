package x.common.component.schedule;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import x.common.util.Once;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
class HandlerXSchedulerImpl implements HandlerXScheduler {
    private static final Future<?> EMPTY = new Future<Object>() {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) { return true; }

        @Override
        public boolean isCancelled() { return true; }

        @Override
        public boolean isDone() { return false; }

        @Override
        public Object get() { return null; }

        @Override
        public Object get(long timeout, TimeUnit unit) { return null; }
    };

    private volatile boolean shutdown = false;
    private final Once<Handler> handler = new Once<>();

    HandlerXSchedulerImpl(@NonNull Handler handler) {
        this.handler.set(Utils.requireNonNull(handler, "handler == null"));
    }

    HandlerXSchedulerImpl() {
    }

    void setHandler(@NonNull Handler handler) {
        this.handler.set(Utils.requireNonNull(handler, "handler == null"));
    }

    @Override
    public void shutdown() {
        shutdown = true;
        handler.get().getLooper().quitSafely();
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void remove(@NonNull Runnable task) {
        handler.get().removeCallbacks(task);
    }

    @Override
    public void removeAll() {
        handler.get().removeCallbacksAndMessages(null);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        Utils.requireNonNull(runnable, "runnable == null");
        if (handler.get().getLooper().getThread() == Thread.currentThread()) {
            runnable.run();
        } else {
            handler.get().post(runnable);
        }
    }

    @NonNull
    @Override
    public Future<?> schedule(@NonNull Runnable runnable, long delay, @NonNull TimeUnit unit) {
        XFutureTask<?> future = new XFutureTask<>(runnable, null, 0);
        return executeDelayed(future, delay, unit);
    }

    @NonNull
    @Override
    public <V> Future<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit) {
        XFutureTask<V> future = new XFutureTask<>(callable);
        return executeDelayed(future, delay, unit);
    }

    @NonNull
    @Override
    public Future<?> scheduleWithFixedDelay(@NonNull Runnable runnable, long initialDelay, long delay, @NonNull TimeUnit unit) {
        XFutureTask<?> future = new XFutureTask<>(runnable, null, unit.toMillis(delay));
        return executeDelayed(future, initialDelay, unit);
    }

    @SuppressWarnings("unchecked")
    private <V> Future<V> executeDelayed(XFutureTask<V> task, long delay, TimeUnit unit) {
        boolean result = handler.get().postDelayed(task, unit.toMillis(delay));
        return result ? task : (Future<V>) EMPTY;
    }

    private boolean canRunInCurrentRunState(boolean periodic) {
        Thread t = handler.get().getLooper().getThread();
        return t.isAlive();
    }


    private final class XFutureTask<V> extends FutureTask<V> {
        private final long period;

        private XFutureTask(@NonNull Runnable r, V result, long period) {
            super(r, result);
            this.period = period;
        }

        private XFutureTask(@NonNull Callable<V> callable) {
            super(callable);
            this.period = 0;
        }

        private boolean isPeriodic() {
            return period != 0;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            boolean canceled = super.cancel(mayInterruptIfRunning);
            if (canceled) handler.get().removeCallbacks(this);
            return canceled;
        }

        @Override
        public void run() {
            boolean periodic = isPeriodic();
            if (!canRunInCurrentRunState(periodic)) {
                cancel(false);
            } else if (!periodic) {
                super.run();
            } else if (super.runAndReset()) {
                Message msg = Message.obtain(handler.get(), this);
                if (!handler.get().sendMessageDelayed(msg, period)) cancel(false);
            }
        }
    }
}
