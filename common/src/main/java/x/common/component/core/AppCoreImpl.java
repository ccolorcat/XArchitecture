package x.common.component.core;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import x.common.IClient;
import x.common.component.SimpleActivityLifecycleCallbacks;
import x.common.component.XObservableImpl;
import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class AppCoreImpl extends XObservableImpl<AppState> implements AppCore {

    private AppCoreImpl(@NonNull IClient client) {
        AppLifecycleListener listener = new AppLifecycleListener();
        client.asAppClient().getApplication().registerActivityLifecycleCallbacks(listener);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(listener);
    }

    @SuppressWarnings("unused")
    public class AppLifecycleListener extends SimpleActivityLifecycleCallbacks implements LifecycleObserver {
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
            update(AppState.FOREGROUNDED);
            logger.d("AppCore.onStart.onAppForegrounded");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onAppBackgrounded() {
            update(AppState.BACKGROUNDED);
            logger.i("AppCore.onStop.onAppBackgrounded");
        }
    }
}
