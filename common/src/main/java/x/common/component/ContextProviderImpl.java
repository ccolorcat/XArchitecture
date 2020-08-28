package x.common.component;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import x.common.IAppClient;
import x.common.IClient;

/**
 * Author: cxx
 * Date: 2020-08-27
 * GitHub: https://github.com/ccolorcat
 */
final class ContextProviderImpl implements ContextProvider {
    private final IAppClient client;
    private Reference<Context> reference;

    private ContextProviderImpl(IClient client) {
        this.client = client.asAppClient();
        this.client.getApplication().registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                super.onActivityResumed(activity);
                reference = new WeakReference<>(activity);
            }
        });
    }

    @NonNull
    @Override
    public Context getApplicationContext() {
        return client.getApplication();
    }

    @Nullable
    @Override
    public Context getActivityContext() {
        return reference != null ? reference.get() : null;
    }
}
