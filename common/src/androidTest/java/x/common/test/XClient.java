package x.common.test;

import android.app.Application;
import android.app.Instrumentation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.test.platform.app.InstrumentationRegistry;

import x.common.IAppClient;
import x.common.component.Hummingbird;
import x.common.component.Lazy;

/**
 * Author: cxx
 * Date: 2020-08-07
 * GitHub: https://github.com/ccolorcat
 */
public class XClient implements IAppClient {
    private final Lazy<ViewModelStore> modelStore = Lazy.by(ViewModelStore::new);
    private Application application;

    public XClient() {
        try {
            this.application = Instrumentation.newApplication(XClient.class, InstrumentationRegistry.getInstrumentation().getTargetContext());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void onCreate() {
        Hummingbird.init(this);
    }

    @NonNull
    @Override
    public Application getApplication() {
        return application;
    }

    @NonNull
    @Override
    public ViewModelProvider.AndroidViewModelFactory getAndroidViewModelFactory() {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return modelStore.get();
    }

    @NonNull
    @Override
    public String getBaseUrl() {
        return "https://www.baidu.com/";
    }

    @Override
    public boolean loggable() {
        return true;
    }
}
