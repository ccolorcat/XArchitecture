package x.common.util.stream;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public final class CollectionSupplier<T> extends BaseSupplier<T> {
    private final long size;
    private final Iterator<T> iterator;

    public CollectionSupplier(@NonNull Collection<T> collection) {
        size = collection.size();
        iterator = collection.iterator();
    }

    @Override
    public boolean hasNext() throws Throwable {
        super.hasNext();
        return iterator.hasNext();
    }

    @Override
    public T next() throws Throwable {
        super.next();
        return iterator.next();
    }

    @Override
    public long size() {
        return size;
    }
}
