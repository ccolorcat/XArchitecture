package x.common.component.core;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import x.common.IClient;
import x.common.component.XObservableImpl;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class AppCoreImpl extends XObservableImpl<AppState> implements AppCore {

    private AppCoreImpl(@NonNull IClient client) {
        AppLifecycleListener listener = new AppLifecycleListener();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(listener);
    }

    @SuppressWarnings("unused")
    public class AppLifecycleListener implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onAppForegrounded() {
            update(AppState.FOREGROUNDED);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onAppBackgrounded() {
            update(AppState.BACKGROUNDED);
        }
    }
}
