package x.common.component.core;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.SimpleActivityLifecycleCallbacks;
import x.common.component.XObservableImpl;
import x.common.component.schedule.MainXScheduler;


/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class AccountCoreImpl extends XObservableImpl<AccountState> implements AccountCore {

    private AccountCoreImpl(@NonNull IClient client) {
        super(Hummingbird.visit(MainXScheduler.class));
        client.asAppClient().getApplication().registerActivityLifecycleCallbacks(new ActivityTracker());
    }

    @Override
    public void login() {
    }

    @Override
    public void logout() {
    }

    private class ActivityTracker extends SimpleActivityLifecycleCallbacks {
        private int count = 0;

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
            if (count == 0) update(AccountState.INITIALIZED);
            ++count;
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            super.onActivityDestroyed(activity);
            --count;
            if (count == 0) update(AccountState.QUITTED);
        }
    }
}
