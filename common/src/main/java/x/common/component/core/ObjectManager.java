package x.common.component.core;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedHashSet;

/**
 * Author: cxx
 * Date: 2020-07-21
 * GitHub: https://github.com/ccolorcat
 */
public class ObjectManager<T> {
    private final Handler mHandler;
    private final LinkedHashSet<ObjectChangeListener<? super T>> mListeners = new LinkedHashSet<>();
    private volatile T mObject;

    public ObjectManager() {
        this(null);
    }

    public ObjectManager(Handler handler) {
        mHandler = handler;
    }

    @Nullable
    public T getObject() {
        return mObject;
    }

    public void update(@NonNull T object) {
        if (mObject != requireNonNull(object, "object == null")) {
            mObject = object;
            dispatchObjectChanged();
        }
    }

    public boolean register(ObjectChangeListener<? super T> listener) {
        if (listener == null) return false;
        synchronized (mListeners) { return mListeners.add(listener); }
    }

    public boolean unregister(ObjectChangeListener<? super T> listener) {
        if (listener == null) return false;
        synchronized (mListeners) { return mListeners.remove(listener); }
    }

    private void dispatchObjectChanged() {
        if (mHandler == null || mHandler.getLooper() == Looper.myLooper()) {
            notifyObjectChanged();
        } else {
            mHandler.post(this::notifyObjectChanged);
        }
    }

    private void notifyObjectChanged() {
        synchronized (mListeners) {
            final T object = mObject;
            for (ObjectChangeListener<? super T> listener : mListeners) {
                listener.onObjectChanged(object);
            }
        }
    }

    public interface ObjectChangeListener<T> {
        void onObjectChanged(@NonNull T object);
    }

    private static <T> T requireNonNull(T value, String msg) {
        if (value == null) throw new NullPointerException(msg);
        return value;
    }
}
