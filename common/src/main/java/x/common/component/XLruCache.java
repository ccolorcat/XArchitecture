package x.common.component;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import x.common.util.Utils;
import x.common.util.function.Producer;
import x.common.util.function.ThrowableProducer;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public final class XLruCache<K, V> extends LruCache<K, V> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public XLruCache(int maxSize) {
        super(maxSize);
    }

    public final V getOrPut(@NonNull K key, @NonNull Producer<? extends V> producer) {
        V value = get(key);
        if (value == null) {
            synchronized (this) {
                if ((value = get(key)) == null) {
                    value = Utils.requireNonNull(producer.apply(), "producer returned null");
                    put(key, value);
                }
            }
        }
        return value;
    }

    public final V unsafeGetOrPut(@NonNull K key, @NonNull ThrowableProducer<? extends V> producer) throws Throwable {
        V value = get(key);
        if (value == null) {
            synchronized (this) {
                if ((value = get(key)) == null) {
                    value = Utils.requireNonNull(producer.apply(), "producer returned null");
                    put(key, value);
                }
            }
        }
        return value;
    }
}
