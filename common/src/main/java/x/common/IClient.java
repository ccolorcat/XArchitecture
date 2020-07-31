package x.common;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-21
 * GitHub: https://github.com/ccolorcat
 */
public interface IClient {
    @NonNull
    String getBaseUrl();

    boolean isTest();
}
