package x.common.component.monitor;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import x.common.IClient;
import x.common.component.XObserver;

/**
 * Author: cxx
 * Date: 2020-08-05
 * GitHub: https://github.com/ccolorcat
 */
final class NetworkMonitorCompat implements NetworkMonitor {
    private final NetworkMonitor delegate;

    private NetworkMonitorCompat(@NonNull IClient client) {
        delegate = newNetworkMonitor(client);
    }

    @Override
    public boolean isAvailable() {
        return delegate.isAvailable();
    }

    @Nullable
    @Override
    public Boolean getValue() {
        return delegate.getValue();
    }

    @Override
    public boolean register(boolean receiveSticky, @NonNull XObserver<? super Boolean> observer) {
        return delegate.register(receiveSticky, observer);
    }

    @Override
    public boolean unregister(@NonNull XObserver<? super Boolean> observer) {
        return delegate.unregister(observer);
    }

    @Override
    public void bind(boolean receiveSticky, @NonNull LifecycleOwner owner, @NonNull XObserver<? super Boolean> observer) {
        delegate.bind(receiveSticky, owner, observer);
    }

    private static NetworkMonitor newNetworkMonitor(IClient client) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                ? new NetworkMonitorLollipop(client) : new NetworkMonitorLower(client);
    }
}
