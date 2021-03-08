package x.common;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStoreOwner;

import java.io.File;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public interface IAppClient extends IClient, ViewModelStoreOwner {
    @Override
    default IAppClient asAppClient() {
        return this;
    }

    @NonNull
    @Override
    default File cacheDir() {
        return Utils.requireNonNull(getApplication().getExternalCacheDir());
    }

    @NonNull
    @Override
    default File filesDir() {
        return Utils.requireNonNull(getApplication().getExternalFilesDir(null));
    }

    @NonNull
    Application getApplication();
}
