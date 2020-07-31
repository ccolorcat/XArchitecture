package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import retrofit2.Call;
import x.common.util.Once;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-20
 * GitHub: https://github.com/ccolorcat
 */
final class VCallImpl<T> extends MCallImpl<T> implements VCall<T> {
    static <T> VCallImpl<T> create(@NonNull Call<T> call) {
        return new VCallImpl<>(Utils.requireNonNull(call, "call == null"));
    }

    private VCallImpl(@NonNull Call<T> delegate) {
        super(delegate);
    }

    @NonNull
    @Override
    public Discardable enqueue(@NonNull LifecycleOwner owner, @NonNull ApiCallback<? super T> callback) {
        attachLifecycle(owner);
        return enqueue(callback);
    }

    @NonNull
    @Override
    public VCall<T> clone() {
        return new VCallImpl<>(delegate.clone());
    }

    private void attachLifecycle(@NonNull LifecycleOwner owner) {
        LifecycleOwner _owner = Utils.requireNonNull(owner);
        Lifecycle.Event discardEvent = Lifecycle.Event.ON_DESTROY;
        if (owner instanceof Fragment) {
            try {
                _owner = ((Fragment) owner).getViewLifecycleOwner();
            } catch (Throwable t) {
                discardEvent = Lifecycle.Event.ON_STOP;
            }
        }
        _owner.getLifecycle().addObserver(new LifecycleListener<>(once, discardEvent));
    }


    private static class LifecycleListener<T> implements LifecycleEventObserver {
        private final Once<MCallback<T>> once;
        private final Lifecycle.Event discardEvent;

        private LifecycleListener(@NonNull Once<MCallback<T>> once, @NonNull Lifecycle.Event discardEvent) {
            this.once = once;
            this.discardEvent = discardEvent;
        }

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (discardEvent == event && once.hasValue()) {
                once.get().discard();
            }
        }
    }
}
