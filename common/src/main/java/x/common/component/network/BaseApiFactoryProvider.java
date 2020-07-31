package x.common.component.network;

import androidx.annotation.NonNull;

import retrofit2.Retrofit;
import x.common.component.XLruCache;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public abstract class BaseApiFactoryProvider implements ApiFactoryProvider {
    private final XLruCache<String, ApiFactory> factories = new XLruCache<>(8);

    @NonNull
    @Override
    public final ApiFactory of(@NonNull String baseUrl) {
        if (!baseUrl.matches("^(http)(s)?://(.)+(/)$")) {
            throw new IllegalArgumentException("bad baseUrl: " + baseUrl);
        }
        ApiFactory factory = factories.get(baseUrl);
        if (factory == null) {
            factory = create(baseUrl);
            factories.put(baseUrl, factory);
        }
        return factory;
    }

    protected ApiFactory create(final String baseUrl) {
        return new ApiFactory() {
            private final Retrofit retrofit = newRetrofit(baseUrl);

            @NonNull
            @Override
            public <T> T create(Class<T> tClass) {
                return retrofit.create(tClass);
            }
        };
    }

    protected abstract Retrofit newRetrofit(String baseUrl);

//    public  <T> Observable<T> apply(Observable<T> observable) {
//        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public  <T> ObservableTransformer<T, T> transformer() {
//        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//    }
}
