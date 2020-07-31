package x.common.component.network;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import x.common.contract.IBase;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public abstract class ViewCallback<V extends IBase.View, T> implements ApiCallback<T> {
    private final Reference<V> ref;

    public ViewCallback(@NonNull V view) {
        this.ref = new WeakReference<>(Utils.requireNonNull(view, "view == null"));
    }

    @Override
    public final void onStart() {
        V view = ref.get();
        if (view != null && view.isActive()) {
            onStart(view);
        }
    }

    @Override
    public final void onSuccess(@NonNull T data) {
        V view = ref.get();
        if (view != null && view.isActive()) {
            onSuccess(view, data);
        }
    }

    @Override
    public final void onFailure(@NonNull ApiException cause) {
        V view = ref.get();
        if (view != null && view.isActive()) {
            onFailure(view, cause);
        }
    }

    @Override
    public final void onFinish() {
        V view = ref.get();
        if (view != null && view.isActive()) {
            onFinish(view);
            ref.clear();
        }
    }

    @CallSuper
    protected void onStart(@NonNull V view) { }

    @CallSuper
    protected void onSuccess(@NonNull V view, @NonNull T data) { }

    @CallSuper
    protected void onFailure(@NonNull V view, @NonNull ApiException cause) { }

    @CallSuper
    protected void onFinish(@NonNull V view) { }
}
