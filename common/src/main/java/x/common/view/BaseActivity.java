package x.common.view;

import android.content.Context;

import androidx.annotation.NonNull;

import x.common.component.Hummingbird;
import x.common.component.runtime.RuntimeForActivity;
import x.common.contract.IAndroid;


/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public abstract class BaseActivity extends RuntimeForActivity implements IAndroid.View {
    private ShowableController sc;

    @Override
    public final boolean isActive() {
        return !isFinishing();
    }

    @NonNull
    @Override
    public ShowableController sc() {
        if (sc == null) {
            sc = Hummingbird.visit(ShowableController.class);
            getLifecycle().addObserver(sc);
        }
        return sc;
    }

    @NonNull
    @Override
    public Context requireContext() {
        return this;
    }
}
