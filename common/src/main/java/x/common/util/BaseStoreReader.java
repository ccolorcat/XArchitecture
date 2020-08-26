package x.common.util;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-08-25
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public abstract class BaseStoreReader<T> implements StoreReader {
    private static final BaseStoreReader<?> EMPTY = new BaseStoreReader() {

        @Override
        public boolean contains(@NonNull String key) {
            return false;
        }

        @Override
        public String getString(@NonNull String key, String defaultValue) {
            return defaultValue;
        }
    };

    static <T> BaseStoreReader<T> empty() {
        return (BaseStoreReader<T>) EMPTY;
    }


    protected final T store;

    private BaseStoreReader() {
        this.store = null;
    }

    protected BaseStoreReader(@NonNull T store) {
        this.store = Utils.requireNonNull(store, "store == null");
    }

    public T getStore() {
        return store;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' +
                "store=" + store +
                '}';
    }
}
