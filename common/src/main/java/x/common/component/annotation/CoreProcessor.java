package x.common.component.annotation;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import x.common.IClient;
import x.common.component.HummingbirdException;
import x.common.util.Reflects;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public final class CoreProcessor<T> implements AnnotationProcessor<T, Core> {
    private final Map<Class<?>, Object> cached = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull Core annotation, @NonNull IClient client) throws Throwable {
        Object result = cached.get(tClass);
        if (result == null) {
            synchronized (cached) {
                if ((result = cached.get(tClass)) == null) {
                    Class<?> impl = Parsers.requireImpl(tClass, annotation.value(), annotation.className());
                    result = newInstance(impl, client);
                    cached.put(tClass, result);
                }
            }
        }
        return (T) result;
    }

    @NonNull
    private static <T> T newInstance(Class<T> tClass, IClient client) throws Throwable {
        Constructor<T> c = Reflects.quietGetConstructor(tClass, IClient.class);
        if (c != null) {
            c.setAccessible(true);
            return c.newInstance(client);
        }
        c = Reflects.quietGetConstructor(tClass);
        if (c != null) {
            c.setAccessible(true);
            return c.newInstance();
        }
        throw new HummingbirdException(tClass + " must have a constructor with no args or only IClient");
    }
}
