package x.common.component.network;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-26
 * GitHub: https://github.com/ccolorcat
 */
final class CommonInterceptorImpl implements CommonInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_AGENT = "User-Agent";

    private final ConcurrentMap<SignType, TokenProvider> signTypeProviders = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, TokenProvider> signMarkProviders = new ConcurrentHashMap<>();
    private CommonQueriesProvider queriesProvider = Collections::emptyMap;
    private UserAgentProvider uaProvider = () -> "";

    private CommonInterceptorImpl() {
    }

    @Override
    public void setCommonQueriesProvider(@NonNull CommonQueriesProvider provider) {
        this.queriesProvider = Utils.requireNonNull(provider);
    }

    @Override
    public void setUserAgentProvider(@NonNull UserAgentProvider provider) {
        this.uaProvider = Utils.requireNonNull(provider);
    }

    @Override
    public void registerTokenProvider(@NonNull SignType type, @NonNull TokenProvider provider) {
        signTypeProviders.put(Utils.requireNonNull(type), Utils.requireNonNull(provider));
    }

    @Override
    public void unregisterTokenProvider(@NonNull SignType type) {
        signTypeProviders.remove(Utils.requireNonNull(type));
    }

    @Override
    public void mark(@NonNull String mark, @NonNull SignType type) {
        TokenProvider provider = signTypeProviders.get(Utils.requireNonNull(type));
        if (provider != null) {
            signMarkProviders.put(Utils.requireNonNull(mark), provider);
        }
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        final Request request = chain.request();
        final HttpUrl url = request.url();
        final Request.Builder builder = request.newBuilder();

        String userAgent;
        if (request.header(USER_AGENT) == null && Utils.isNotEmpty((userAgent = uaProvider.getUserAgent()))) {
            builder.header(USER_AGENT, userAgent);
        }

        if (request.header(AUTHORIZATION) == null) {
            TokenProvider provider = queryProvider(Urls.getUrlTrunk(url.toString()));
            String token;
            if (provider != null && Utils.isNotEmpty((token = provider.getToken()))) {
                builder.header("Authorization", "Bearer " + token);
            }
        }

        Map<String, String> params = queriesProvider.getCommonQueries();
        if (!params.isEmpty()) {
            HttpUrl.Builder urlBuilder = url.newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            builder.url(urlBuilder.build());
        }

        return chain.proceed(builder.build());
    }

    private TokenProvider queryProvider(String urlTrunk) {
        TokenProvider provider = null;
        for (Map.Entry<String, TokenProvider> entry : signMarkProviders.entrySet()) {
            if (urlTrunk.equals(entry.getKey())) return entry.getValue();
            if (urlTrunk.matches(entry.getKey())) provider = entry.getValue();
        }
        return provider;
    }
}
