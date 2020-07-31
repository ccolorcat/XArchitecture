package x.common.component;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public final class RegistrableBinder<T> implements LifecycleEventObserver {
    public static <T> void bind(@NonNull LifecycleOwner owner, @NonNull Registrable<? super T> registrable, T t) {
//        LifecycleOwner _owner = (owner instanceof Fragment) ? ((Fragment) owner).getViewLifecycleOwner() : owner;
        owner.getLifecycle().addObserver(new RegistrableBinder<>(registrable, t));
    }

    private final Registrable<? super T> registrable;
    private final T t;

    private RegistrableBinder(@NonNull Registrable<? super T> registrable, T t) {
        this.registrable = Utils.requireNonNull(registrable, "registrable == null");
        this.t = t;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                registrable.register(t);
                break;
            case ON_DESTROY:
                registrable.unregister(t);
                break;
            default:
                break;
        }
    }
}
