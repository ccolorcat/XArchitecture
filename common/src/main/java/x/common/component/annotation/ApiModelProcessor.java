package x.common.component.annotation;


import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.XLruCache;
import x.common.component.network.ApiFactoryProvider;
import x.common.util.Reflects;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */

@SuppressWarnings("unchecked")
public final class ApiModelProcessor<T> implements AnnotationProcessor<T, ApiModel> {
    private final XLruCache<Class<?>, Object> cached = new XLruCache<>(8);

    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull ApiModel annotation, @NonNull IClient client) throws Throwable {
        Object result = cached.get(tClass);
        if (result == null) {
            result = create(tClass, annotation, client);
            cached.put(tClass, result);
        }
        return (T) result;
    }

    @NonNull
    private static <T> T create(Class<T> tClass, ApiModel annotation, IClient client) throws Throwable {
        Class<?> impl = annotation.value();
        if (impl != Void.class && Checker.assertImpl(tClass, impl)) return (T) Reflects.newDefaultInstance(impl);
        String baseUrl = Utils.emptyElse(annotation.baseUrl(), client.getBaseUrl());
        return Hummingbird.visit(ApiFactoryProvider.class).of(baseUrl).create(tClass);
    }
}
