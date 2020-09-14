package x.common.component.network;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import x.common.component.Hummingbird;
import x.common.component.core.ClientInfoProvider;
import x.common.component.core.LocationCore;
import x.common.component.core.XLocation;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-26
 * GitHub: https://github.com/ccolorcat
 */
final class CommonInterceptorImpl implements CommonInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_AGENT = "User-Agent";

    private final ConcurrentMap<SignType, AuthorizationProvider> signTypeProviders = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AuthorizationProvider> signMarkProviders = new ConcurrentHashMap<>();
    private final ClientInfoProvider clientInfoProvider = Hummingbird.visit(ClientInfoProvider.class);
    private final LocationCore locationCore = Hummingbird.visit(LocationCore.class);

    private CommonInterceptorImpl() {
    }

    @Override
    public void registerAuthorizationProvider(@NonNull SignType type, @NonNull AuthorizationProvider provider) {
        signTypeProviders.put(Utils.requireNonNull(type), Utils.requireNonNull(provider));
    }

    @Override
    public void unregisterAuthorizationProvider(@NonNull SignType type) {
        signTypeProviders.remove(Utils.requireNonNull(type));
    }

    @Override
    public boolean mark(@NonNull String mark, @NonNull SignType type) {
        AuthorizationProvider provider = signTypeProviders.get(Utils.requireNonNull(type));
        if (provider != null) {
            signMarkProviders.put(Utils.requireNonNull(mark), provider);
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Request request = chain.request();
        final HttpUrl url = request.url();
        final Request.Builder builder = request.newBuilder();

        String userAgent;
        if (request.header(USER_AGENT) == null && Utils.isNotEmpty((userAgent = getUserAgent()))) {
            builder.header(USER_AGENT, userAgent);
        }

        if (request.header(AUTHORIZATION) == null) {
            AuthorizationProvider provider = queryProvider(HttpUtils.getUrlTrunk(url.toString()));
            String authorization;
            if (provider != null && Utils.isNotEmpty((authorization = provider.getAuthorization()))) {
                builder.header(AUTHORIZATION, authorization);
            }
        }

        Map<String, String> params = getCommonQueries();
        if (!params.isEmpty()) {
            HttpUrl.Builder urlBuilder = url.newBuilder();
            String key, value;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if (Utils.isNotEmpty(key) && Utils.isNotEmpty(value)) {
                    urlBuilder.addQueryParameter(key, value);
                }
            }
            builder.url(urlBuilder.build());
        }

        return chain.proceed(builder.build());
    }

    private AuthorizationProvider queryProvider(String urlTrunk) {
        AuthorizationProvider provider = null;
        for (Map.Entry<String, AuthorizationProvider> entry : signMarkProviders.entrySet()) {
            if (urlTrunk.equals(entry.getKey())) return entry.getValue();
            if (urlTrunk.matches(entry.getKey())) provider = entry.getValue();
        }
        return provider;
    }

    @NonNull
    private String getUserAgent() {
        return clientInfoProvider.getClientInfo().asUserAgent();
    }

    @NonNull
    private Map<String, String> getCommonQueries() {
        XLocation location = locationCore.getValue();
        String lng = "", lat = "";
        if (location != null) {
            lng = location.getLongitude();
            lat = location.getLatitude();
        }
        return clientInfoProvider.getClientInfo().asCommonQueries(System.currentTimeMillis(), lng, lat);
    }
}
