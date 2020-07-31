package x.common.contract;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
final class ViewPresenterBinder<V extends IBase.View> implements LifecycleEventObserver {
    private final IBase.Presenter<?> presenter;
    private final V view;

    ViewPresenterBinder(@NonNull V vew, @NonNull IBase.Presenter<?> presenter) {
        this.view = Utils.requireNonNull(vew, "vew == null");
        this.presenter = Utils.requireNonNull(presenter, "presenter == null");
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                presenter.onCreate(view);
                break;
            case ON_DESTROY:
                presenter.onDestroy();
                break;
            default:
                break;
        }
    }
}
