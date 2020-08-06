package x.common;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Author: cxx
 * Date: 2020-07-21
 * GitHub: https://github.com/ccolorcat
 */
public interface IClient {
    @NonNull
    String getBaseUrl();

    @NonNull
    File cacheDir();

    @NonNull
    File filesDir();

    boolean isTest();

    IAppClient asAppClient();
}
