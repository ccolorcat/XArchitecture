package x.common.component.annotation;


import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.component.XLruCache;
import x.common.util.Reflects;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public final class StatelessProcessor<T> implements AnnotationProcessor<T, Stateless> {
    private final XLruCache<Class<?>, Object> cached = new XLruCache<>(16);

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull Stateless annotation, @NonNull IClient client) throws Throwable {
        return (T) cached.unsafeGetOrPut(tClass, () -> {
            Class<?> impl = Parsers.requireImpl(tClass, annotation.value(), annotation.className());
            return Reflects.newDefaultInstance(impl);
        });
    }
}
