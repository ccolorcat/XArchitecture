package x.common.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.LifecycleOwner;

import x.common.component.Hummingbird;
import x.common.component.core.PageTracker;
import x.common.component.log.Logger;
import x.common.contract.IAndroid;


/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public abstract class BaseDialogFragment extends AppCompatDialogFragment implements IAndroid.View, OnBackPressListener {
    private ShowableController sc;
    private boolean mActive = false;

    @LayoutRes
    protected int getLayoutId() {
        return -1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(Hummingbird.visit(PageTracker.class));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId != -1) view = inflater.inflate(layoutId, container, false);
        return view;
    }

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActive = true;
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        mActive = false;
        super.onDestroyView();
    }

    @Override
    public boolean isActive() {
        return mActive;
    }

    @NonNull
    @Override
    public ShowableController sc() {
        if (sc == null) {
            sc = Hummingbird.visit(ShowableController.class);
            LifecycleOwner owner = this;
            try {
                owner = getViewLifecycleOwner();
            } catch (Throwable t) {
                Logger.getDefault().e(t);
            }
            owner.getLifecycle().addObserver(sc);
        }
        return sc;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
