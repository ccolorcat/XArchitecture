package x.common.component.core;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import x.common.contract.IBase;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
final class ViewOwnerImpl implements ViewOwner {
    private final Map<Object, IBase.View> mCachedView = new HashMap<>();

    private ViewOwnerImpl() {}

    @NonNull
    @Override
    public <V extends IBase.View> V require(@NonNull Object tag) {
        return Utils.requireNonNull(get(tag), "no cache view by " + tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V extends IBase.View> V get(@NonNull Object tag) {
        return (V) mCachedView.get(tag);
    }

    @Override
    public <V extends IBase.View> void add(@NonNull Object tag, @NonNull V view) {
        mCachedView.put(Utils.requireNonNull(tag), Utils.requireNonNull(view));
    }

    @Override
    public void remove(@NonNull Object tag) {
        mCachedView.remove(Utils.requireNonNull(tag));
    }

    @NonNull
    @Override
    public String toString() {
        return "ViewOwnerImpl{" +
                "mCachedView=" + mCachedView +
                '}';
    }
}
