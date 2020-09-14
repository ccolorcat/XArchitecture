package x.common.component.network;

import androidx.annotation.NonNull;

import okhttp3.Interceptor;
import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-08-26
 * GitHub: https://github.com/ccolorcat
 */
@Core(CommonInterceptorImpl.class)
public interface CommonInterceptor extends Interceptor {
    void registerAuthorizationProvider(@NonNull SignType type, @NonNull AuthorizationProvider provider);

    void unregisterAuthorizationProvider(@NonNull SignType type);

    boolean mark(@NonNull String mark, @NonNull SignType type);
}
