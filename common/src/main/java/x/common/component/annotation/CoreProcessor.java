package x.common.component.annotation;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import x.common.IClient;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public final class CoreProcessor<T> implements AnnotationProcessor<T, Core> {
    private final Map<Class<?>, Object> mCores = new HashMap<>();

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull Core annotation, @NonNull IClient client) throws Throwable {
        Object t = mCores.get(tClass);
        if (t == null) {
            Class<?> impl = annotation.value();
            if (impl == Void.class || !Checker.assertImpl(tClass, impl)) impl = tClass;
            t = newInstance(impl, client);
            mCores.put(tClass, t);
        }
        return (T) t;
    }

    private static <T> T newInstance(Class<T> tClass, IClient client) throws Throwable {
        Constructor<T> c = Utils.quietGetConstructor(tClass, IClient.class);
        if (c != null) {
            c.setAccessible(true);
            return c.newInstance(client);
        }
        c = Utils.quietGetConstructor(tClass);
        if (c != null) {
            c.setAccessible(true);
            return c.newInstance();
        }
        throw new IllegalArgumentException(tClass + " must have a constructor with no args or only IClient");
    }
}
