package x.common.component;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.LinkedHashSet;

import x.common.component.schedule.XScheduler;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-05
 * GitHub: https://github.com/ccolorcat
 */
public class XObservableImpl<T> implements XObservable<T> {
    protected final LinkedHashSet<XObserver<? super T>> observers = new LinkedHashSet<>();
    protected volatile T value;
    private final XScheduler scheduler;

    protected XObservableImpl() {
        this(null);
    }

    protected XObservableImpl(@Nullable XScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Nullable
    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public boolean register(boolean receiveSticky, @NonNull XObserver<? super T> observer) {
        Utils.requireNonNull(observer, "observer == null");
        synchronized (this.observers) {
            boolean result = this.observers.add(observer);
            if (result && receiveSticky && value != null) observer.onChanged(value);
            return result;
        }
    }

    @Override
    public boolean unregister(@NonNull XObserver<? super T> observer) {
        Utils.requireNonNull(observer, "observer == null");
        synchronized (this.observers) {
            return this.observers.remove(observer);
        }
    }

    @Override
    public void bind(boolean receiveSticky, @NonNull LifecycleOwner owner, @NonNull XObserver<? super T> observer) {
        if (this.observers.contains(Utils.requireNonNull(observer, "observer == null"))) return;
        owner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            switch (event) {
                case ON_CREATE:
                    register(receiveSticky, observer);
                    break;
                case ON_DESTROY:
                    unregister(observer);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public boolean hasObserver(XObserver<? super T> observer) {
        return observer != null && this.observers.contains(observer);
    }

    protected void update(@NonNull T value) {
        if (this.value != Utils.requireNonNull(value, "value == null")) {
            this.value = value;
            if (scheduler == null) {
                notifyValueChanged(value);
            } else {
                scheduler.execute(() -> notifyValueChanged(value));
            }
        }
    }

    protected void notifyValueChanged(@NonNull T value) {
        synchronized (this.observers) {
            for (XObserver<? super T> observer : this.observers) {
                observer.onChanged(value);
            }
        }
    }
}
