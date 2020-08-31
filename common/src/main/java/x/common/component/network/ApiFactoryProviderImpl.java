package x.common.component.network;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public class ApiFactoryProviderImpl extends BaseApiFactoryProvider {
    private final CommonInterceptor interceptor = Hummingbird.visit(CommonInterceptor.class);
    private final Cache cache;
    private final boolean loggable;

    public ApiFactoryProviderImpl(@NonNull IClient client) {
        this.cache = makeCache(client);
        this.loggable = client.loggable();
    }

    @Override
    protected ApiFactory create(String baseUrl) {
        return new SignApiFactory(newRetrofit(baseUrl));
    }

    @Override
    protected Retrofit newRetrofit(String baseUrl) {
//        return RetrofitManager.getRetrofit(baseUrl);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor);
        if (cache != null) builder.cache(cache);
        if (loggable) {
            builder.addInterceptor(createLoggingInterceptor())
                    .addInterceptor(new DelayInterceptor());
        }
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addConverterFactory(MConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new MCallAdapterFactory())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    protected Cache makeCache(IClient client) {
        return null;
    }

    private Interceptor createLoggingInterceptor() {
        return new LoggingInterceptor();
//        return new HttpLoggingInterceptor(log -> Logger.getLogger("OkHttp").i(log))
//                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    private static class CommonParamsInterceptor implements Interceptor {
        private final String userAgent;
        private final AuthorizationProvider provider;

        private CommonParamsInterceptor(String userAgent, AuthorizationProvider provider) {
            this.userAgent = userAgent;
            this.provider = provider;
        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder().header("User-Agent", userAgent);
            String token = provider.getAuthorization();
            if (!Utils.isEmpty(token)) builder.header("Authorization", provider.getAuthorization());
            return chain.proceed(builder.build());
        }
    }


    private static class DelayInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Utils.sleep(TimeUnit.SECONDS, 3);
            return chain.proceed(chain.request());
        }
    }
}
