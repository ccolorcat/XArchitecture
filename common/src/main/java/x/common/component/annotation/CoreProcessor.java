package x.common.component.annotation;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import x.common.IClient;
import x.common.util.Reflects;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public final class CoreProcessor<T> implements AnnotationProcessor<T, Core> {
    private final Map<Class<?>, Object> cached = new HashMap<>();

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull Core annotation, @NonNull IClient client) throws Throwable {
        Object result = cached.get(tClass);
        if (result == null) {
            Class<?> impl = annotation.value();
            if (impl == Void.class || !Checker.assertImpl(tClass, impl)) impl = tClass;
            result = newInstance(impl, client);
            cached.put(tClass, result);
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
        throw new IllegalArgumentException(tClass + " must have a constructor with no args or only IClient");
    }
}
