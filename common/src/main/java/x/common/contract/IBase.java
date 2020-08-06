package x.common.contract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import x.common.component.Hummingbird;
import x.common.component.core.ViewOwner;
import x.common.view.LoadingController;
import x.common.view.ShowableController;


/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public interface IBase {
    interface View extends LoadingController, LifecycleOwner {
        boolean isActive();

        @NonNull
        ShowableController sc();

        @NonNull
        default <P extends Presenter<?>> P bindAndGet(@NonNull Class<P> pClass) {
            P p = Hummingbird.visit(pClass);
            getLifecycle().addObserver(p);
            return p;
        }
    }

    interface Presenter<V extends View> extends LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        default void onCreate(@NonNull LifecycleOwner owner) {
            try {
                Hummingbird.visit(ViewOwner.class).add(this, (View) owner);
            } catch (ClassCastException ex) {
                throw new RuntimeException(owner + " must be implemented properly view");
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        default void onDestroy() {
            Hummingbird.visit(ViewOwner.class).remove(this);
        }

        @Nullable
        default V getView() {
            return Hummingbird.visit(ViewOwner.class).get(this);
        }

        @NonNull
        default V requireView() {
            return Hummingbird.visit(ViewOwner.class).require(this);
        }
    }
}
