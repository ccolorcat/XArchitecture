package x.common.component.store;


import androidx.annotation.NonNull;

import x.common.IClient;
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

    protected BaseStoreFactoryProvider(@NonNull IClient client) {
    }

    @NonNull
    @Override
    public final StoreFactory of(@NonNull String name) {
        if (Utils.isEmpty(name)) throw new IllegalArgumentException("name is empty");
        return factories.getOrPut(name, () -> create(name));
    }

    @NonNull
    protected StoreFactory create(final String name) {
        return new StoreFactoryImpl(newStore(name), serializer);
    }

    @NonNull
    protected abstract Store newStore(String name);
}
