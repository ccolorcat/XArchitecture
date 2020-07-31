package x.common.component.annotation;

import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.XLruCache;
import x.common.component.store.StoreFactoryProvider;


/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("unchecked")
public final class StoreModelProcessor<T> implements AnnotationProcessor<T, StoreModel> {
    private final XLruCache<Class<?>, Object> mCaches = new XLruCache<>(4);

    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull StoreModel annotation, @NonNull IClient client) throws Throwable {
        Object result = mCaches.get(tClass);
        if (result == null) {
            result = create(tClass, annotation);
            mCaches.put(tClass, result);
        }
        return (T) result;
    }

    private static <T> T create(Class<T> tClass, StoreModel annotation) throws Throwable {
        Class<?> impl = annotation.value();
        if (impl != Void.class && Checker.assertImpl(tClass, impl)) return (T) impl.newInstance();
        String name = annotation.name();
        return Hummingbird.visit(StoreFactoryProvider.class).of(name).create(tClass);
    }
}
