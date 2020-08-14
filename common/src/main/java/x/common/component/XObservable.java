package x.common.component;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("UnusedReturnValue")
public interface XObservable<T> {
    @Nullable
    T getValue();

    default boolean register(@NonNull XObserver<? super T> observer) {
        return register(false, observer);
    }

    boolean register(boolean receiveSticky, @NonNull XObserver<? super T> observer);

    boolean unregister(@NonNull XObserver<? super T> observer);

    default void bind(@NonNull LifecycleOwner owner, @NonNull XObserver<? super T> observer) {
        bind(false, owner, observer);
    }

    void bind(boolean receiveSticky, @NonNull LifecycleOwner owner, @NonNull XObserver<? super T> observer);

    boolean hasObserver(XObserver<? super T> observer);
}
