package x.common.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.Map;

import x.common.util.function.Producer;
import x.common.component.XLruCache;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public final class ShowableControllerImpl implements ShowableController, LifecycleEventObserver {
    private XLruCache<String, Showable> mCached = new XLruCache<>(4);
    private final Lifecycle.Event mTiming;

    private ShowableControllerImpl() {
        this(Lifecycle.Event.ON_DESTROY);
    }

    private ShowableControllerImpl(@NonNull Lifecycle.Event timing) {
        mTiming = Utils.requireNonNull(timing, "timing == null");
    }

    @Override
    public boolean show(@NonNull String key) {
        return show(key, null);
    }

    @Override
    public boolean show(@NonNull String key, Producer<? extends Showable> producer) {
        Showable showable = mCached.get(Utils.requireNonNull(key, "key == null"));
        if (showable == null && producer != null) {
            showable = Utils.requireNonNull(producer.apply());
            mCached.put(key, showable);
        }
        if (showable != null) {
            showable.show();
            return showable.isShowing();
        }
        return false;
    }

    @Override
    public void dismiss(@NonNull String key) {
        dismiss(key, true);
    }

    @Override
    public void dismiss(@NonNull String key, boolean remove) {
        Showable showable = mCached.get(Utils.requireNonNull(key, "key == null"));
        if (showable != null) {
            showable.dismiss();
            if (remove) mCached.remove(key);
        }
    }

    @Override
    public boolean isShowing(@NonNull String key) {
        Showable showable = mCached.get(Utils.requireNonNull(key, "key == null"));
        return showable != null && showable.isShowing();
    }

    @Override
    public boolean isAnyShowing() {
        for (Map.Entry<String, Showable> entry : mCached.snapshot().entrySet()) {
            if (entry.getValue().isShowing()) return true;
        }
        return false;
    }

    private void dismissAll() {
        for (Map.Entry<String, Showable> entry : mCached.snapshot().entrySet()) {
            entry.getValue().dismiss();
        }
        mCached.evictAll();
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (mTiming == event) {
            dismissAll();
        }
    }
}
