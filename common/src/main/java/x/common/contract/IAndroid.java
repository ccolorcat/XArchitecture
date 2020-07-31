package x.common.contract;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import x.common.component.Hummingbird;
import x.common.component.log.Logger;
import x.common.view.LoadingCreator;


/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public interface IAndroid {
    interface View extends IBase.View {
        @Override
        default void showLoading() {
            sc().show("loading.common.ui", () -> Hummingbird.visit(LoadingCreator.class).create(requireContext()));
        }

        @Override
        default void hideLoading() {
            sc().dismiss("loading.common.ui");
        }

        @NonNull
        Context requireContext();

        @NonNull
        @Override
        default <P extends IBase.Presenter<?>> P bindAndGet(@NonNull Class<P> pClass) {
            final P p = Hummingbird.visit(pClass);
            LifecycleObserver observer = p;
            LifecycleOwner owner = this;
            if (this instanceof Fragment) {
                try {
                    owner = ((Fragment) this).getViewLifecycleOwner();
                    observer = new ViewPresenterBinder<>(this, p);
                } catch (Throwable t) {
                    Logger.getDefault().e(t);
                }
            }
            owner.getLifecycle().addObserver(observer);
            return p;
        }
    }
}
