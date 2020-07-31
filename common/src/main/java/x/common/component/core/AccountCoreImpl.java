package x.common.component.core;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.LinkedHashSet;

import x.common.IAppClient;
import x.common.IClient;
import x.common.component.AutoRegistrable;
import x.common.component.Hummingbird;
import x.common.component.SimpleActivityLifecycleCallbacks;
import x.common.component.schedule.MainXScheduler;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public final class AccountCoreImpl implements AccountCore, AutoRegistrable<AccountStateListener> {
    private final LinkedHashSet<AccountStateListener> mListeners = new LinkedHashSet<>();
    private AccountState mAccountState;

    private AccountCoreImpl(@NonNull IClient client) {
        IAppClient _client = (IAppClient) client;
        _client.getApplication().registerActivityLifecycleCallbacks(new ActivityTracker());
    }

    public void login() {

    }


    public void logout() {

    }

    @Override
    public boolean register(AccountStateListener listener) {
        if (listener == null) return false;
        synchronized (mListeners) {
            boolean result = mListeners.add(listener);
            AccountState state;
            if (result && (state = mAccountState) != null) listener.onAccountStateChanged(state);
            return result;
        }
    }

    @Override
    public boolean unregister(AccountStateListener listener) {
        if (listener == null) return false;
        synchronized (mListeners) { return mListeners.remove(listener); }
    }

    @Override
    public void bind(@NonNull LifecycleOwner owner, @NonNull AccountStateListener listener) {
        if (mListeners.contains(Utils.requireNonNull(listener, "listener == null"))) return;
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

    private void updateAccountState(@NonNull AccountState state) {
        if (mAccountState != Utils.requireNonNull(state, "state == null")) {
            mAccountState = state;
            dispatchAccountStateChanged();
        }
    }

    private void dispatchAccountStateChanged() {
        Hummingbird.visit(MainXScheduler.class).execute(this::notifyAccountStateChanged);
    }

    private void notifyAccountStateChanged() {
        AccountState state = mAccountState;
        synchronized (mListeners) {
            for (AccountStateListener listener : mListeners) {
                listener.onAccountStateChanged(state);
            }
        }
    }

    private class ActivityTracker extends SimpleActivityLifecycleCallbacks {
        private int count = 0;

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
            if (count == 0) updateAccountState(AccountState.STARTED);
            ++count;
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            super.onActivityDestroyed(activity);
            --count;
            if (count == 0) updateAccountState(AccountState.STOPPED);
        }
    }
}
