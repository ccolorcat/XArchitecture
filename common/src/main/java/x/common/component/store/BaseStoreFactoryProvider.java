package x.common.component.store;


import androidx.annotation.NonNull;

import java.lang.reflect.Proxy;

import x.common.component.XLruCache;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public abstract class BaseStoreFactoryProvider implements StoreFactoryProvider {
    private final XLruCache<String, StoreFactory> factories = new XLruCache<>(4);
    private final StoreSerializer serializer = new JsonStoreSerializer();

    @NonNull
    @Override
    public final StoreFactory of(@NonNull String name) {
        if (Utils.isEmpty(name)) throw new IllegalArgumentException("name is empty");
        StoreFactory factory = factories.get(name);
        if (factory == null) {
            factory = create(name);
            factories.put(name, factory);
        }
        return factory;
    }

    @SuppressWarnings("unchecked")
    protected StoreFactory create(final String name) {
        return new StoreFactory() {
            @NonNull
            @Override
            public <T> T create(@NonNull Class<T> tClass) {
                return (T) Proxy.newProxyInstance(
                        tClass.getClassLoader(),
                        new Class[]{tClass},
                        new StoreHandler(newStore(name), serializer)
                );
            }
        };
    }

    @NonNull
    protected abstract Store newStore(String name);
}
