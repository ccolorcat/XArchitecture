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
    void setCommonQueriesProvider(@NonNull CommonQueriesProvider provider);

    void setUserAgentProvider(@NonNull UserAgentProvider provider);

    void registerTokenProvider(@NonNull SignType type, @NonNull TokenProvider provider);

    void unregisterTokenProvider(@NonNull SignType type);

    void mark(@NonNull String mark, @NonNull SignType type);
}
