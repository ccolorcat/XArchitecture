package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import x.common.IClient;
import x.common.component.Hummingbird;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public class ApiFactoryProviderImpl extends BaseApiFactoryProvider {
    private final CommonInterceptor commonInterceptor = Hummingbird.visit(CommonInterceptor.class);
    private final Cache cache;
    private final boolean loggable;

    public ApiFactoryProviderImpl(@NonNull IClient client) {
        this.cache = makeCache(client);
        this.loggable = client.loggable();
    }

    @NonNull
    @Override
    protected ApiFactory create(String baseUrl) {
        return new SignApiFactory(newRetrofit(baseUrl));
    }

    @NonNull
    @Override
    protected Retrofit newRetrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(commonInterceptor);
        if (cache != null) builder.cache(cache);
        if (loggable) {
            builder.addInterceptor(createLoggingInterceptor());
        }
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addConverterFactory(MConverterFactory.create())
                .addCallAdapterFactory(new MCallAdapterFactory())
                .build();
    }

    @Nullable
    protected Cache makeCache(IClient client) {
        return null;
    }

    @NonNull
    protected Interceptor createLoggingInterceptor() {
        return new LoggingInterceptor();
    }
}
