package x.common.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import x.common.component.Hummingbird;
import x.common.component.core.PageTracker;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(Hummingbird.visit(PageTracker.class));
    }

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

    @Override
    public void onBackPressed() {
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (handleBackPressed(f)) return;
        }
        super.onBackPressed();
    }

    private boolean handleBackPressed(Fragment fragment) {
        return fragment instanceof OnBackPressListener
                && fragment.getActivity() == this
                && fragment.isVisible()
                && ((OnBackPressListener) fragment).onBackPressed();
    }
}
