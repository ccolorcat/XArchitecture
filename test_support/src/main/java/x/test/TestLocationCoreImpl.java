package x.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import x.common.component.XObserver;
import x.common.component.core.LocationCore;
import x.common.component.core.XLocation;

/**
 * Author: cxx
 * Date: 2021-03-05
 * GitHub: https://github.com/ccolorcat
 */
public class TestLocationCoreImpl implements LocationCore {
    @Override
    public void start() {

    }

    @Override
    public void requestLocation() {

    }

    @Override
    public void stop() {

    }

    @Nullable
    @Override
    public XLocation getValue() {
        return null;
    }

    @Override
    public boolean register(boolean receiveSticky, @NonNull XObserver<? super XLocation> observer) {
        return false;
    }

    @Override
    public boolean unregister(@NonNull XObserver<? super XLocation> observer) {
        return false;
    }

    @Override
    public void bind(boolean receiveSticky, @NonNull LifecycleOwner owner, @NonNull XObserver<? super XLocation> observer) {

    }

    @Override
    public boolean hasObserver(XObserver<? super XLocation> observer) {
        return false;
    }
}
