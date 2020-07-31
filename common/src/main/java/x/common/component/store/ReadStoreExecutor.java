package x.common.component.store;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-26
 * GitHub: https://github.com/ccolorcat
 */
final class ReadStoreExecutor implements StoreExecutor {
    private final Store store;
    private final StoreSerializer serializer;
    private final String key;
    private final Type returnType;

    ReadStoreExecutor(@NonNull Store store, @NonNull StoreSerializer serializer, @NonNull String key, @NonNull Type returnType) {
        this.store = Utils.requireNonNull(store, "store == null");
        this.serializer = Utils.requireNonNull(serializer, "serializer == null");
        this.key = Utils.requireNonNull(key, "key == null");
        this.returnType = Utils.requireNonNull(returnType, "returnType == null");
    }

    @Override
    public Object execute(Object[] args) {
        if (args != null && args.length > 1) {
            throw new AssertionError("the number of args error");
        }
        String value = store.read(key);
        if (value == null && args != null && args.length != 0) return args[0];
        return serializer.deserialize(value, returnType);
    }
}
