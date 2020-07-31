package x.common;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public interface IAppClient extends IClient, ViewModelStoreOwner {
    @NonNull
    Application getApplication();

    @NonNull
    ViewModelProvider.AndroidViewModelFactory getAndroidViewModelFactory();
}
