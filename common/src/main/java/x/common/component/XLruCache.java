package x.common.component;

import androidx.collection.LruCache;

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
}
