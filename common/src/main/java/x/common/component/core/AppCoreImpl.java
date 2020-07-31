package x.common.component.core;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.LinkedHashSet;

import x.common.IAppClient;
import x.common.IClient;
import x.common.component.AutoRegistrable;
import x.common.component.SimpleActivityLifecycleCallbacks;
import x.common.component.log.Logger;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public final class AppCoreImpl implements AppCore, AutoRegistrable<AppStateListener> {
    private final LinkedHashSet<AppStateListener> stateListener = new LinkedHashSet<>();
    private AppState appState;

    private AppCoreImpl(@NonNull IClient client) {
        IAppClient _client = (IAppClient) client;
        AppLifecycleListener listener = new AppLifecycleListener();
        _client.getApplication().registerActivityLifecycleCallbacks(listener);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(listener);
    }

    @Override
    public boolean register(AppStateListener listener) {
        if (listener == null) return false;
        synchronized (stateListener) {
            return stateListener.add(listener);
        }
    }

    @Override
    public boolean unregister(AppStateListener listener) {
        if (listener == null) return false;
        synchronized (stateListener) { return stateListener.remove(listener);}
    }

    @Override
    public void bind(@NonNull LifecycleOwner owner, @NonNull AppStateListener listener) {
        if (stateListener.contains(Utils.requireNonNull(listener, "listener == null"))) return;
        owner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            switch (event) {
                case ON_CREATE:
                    register(listener);
                    break;
                case ON_DESTROY:
                    unregister(listener);
                    break;
                default:
                    break;
            }
        });
    }

    private void updateAppState(@NonNull AppState state) {
        if (appState != Utils.requireNonNull(state, "state == null")) {
            appState = state;
            synchronized (stateListener) {
                for (AppStateListener listener : stateListener) {
                    listener.onAppStateChanged(state);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private class AppLifecycleListener extends SimpleActivityLifecycleCallbacks implements LifecycleObserver {
        private final Logger logger = Logger.getLogger("AppCore");
        private int count = 0;

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
            if (count == 0) logger.v("onLaunched");
            ++count;
            logger.v("AppCore.onActivityCreated " + count + ", " + activity);
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            super.onActivityResumed(activity);
            logger.v("AppCore.onActivityResumed" + ", " + activity);
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            super.onActivityPaused(activity);
            logger.v("AppCore.onActivityPaused" + ", " + activity);
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            super.onActivityDestroyed(activity);
            --count;
            if (count == 0) logger.v("onFinished");
            logger.v("AppCore.onActivityDestroyed " + count + ", " + activity);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void onAppCreated() {
            logger.v("AppCore.onCreate.onAppCreated");
        }


        @OnLifecycleEvent(Lifecycle.Event.ON_START)
//        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onAppForegrounded() {
            updateAppState(AppState.FOREGROUNDED);
            logger.d("AppCore.onStart.onAppForegrounded");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onAppBackgrounded() {
            updateAppState(AppState.BACKGROUNDED);
            logger.i("AppCore.onStop.onAppBackgrounded");
        }
    }
}
