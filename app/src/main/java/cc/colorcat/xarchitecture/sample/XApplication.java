package cc.colorcat.xarchitecture.sample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;

import x.common.IAppClient;
import x.common.component.Hummingbird;
import x.common.component.XObserver;
import x.common.component.core.AccountCore;
import x.common.component.core.AppCore;
import x.common.component.core.LocationCore;
import x.common.component.core.XLocation;
import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
public class XApplication extends Application implements IAppClient {
    private final ViewModelStore mGlobalVmStore = new ViewModelStore();
    private final XObserver<XLocation> mLocationObserver = value -> Logger.getLogger("LocationCore").i("LocationCore.onChange: " + value);

    @Override
    public void onCreate() {
        super.onCreate();
        Hummingbird.init(this);
        Hummingbird.visit(AppCore.class);
        Hummingbird.visit(AccountCore.class);
        Hummingbird.visit(LocationCore.class).register(true, mLocationObserver);
    }

    @NonNull
    @Override
    public Application getApplication() {
        return this;
    }

    @NonNull
    @Override
    public String getBaseUrl() {
        return "https://www.baidu.com/";
    }

    @Override
    public boolean loggable() {
        return BuildConfig.DEBUG;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mGlobalVmStore;
    }
}
