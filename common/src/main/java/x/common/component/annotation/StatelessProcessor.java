package x.common.component.annotation;


import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.component.XLruCache;
import x.common.util.Reflects;
import x.common.util.Utils;


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
        Object result = cached.get(tClass);
        if (result == null) {
            Class<?> impl = annotation.value();
            if (impl == Void.class || !Checker.assertImpl(tClass, impl)) {
                String className = annotation.className();
                impl = Utils.isEmpty(className) ? tClass : Class.forName(className);
            }
            result = Reflects.newDefaultInstance(impl);
            cached.put(tClass, result);
        }
        return (T) result;
    }
}
