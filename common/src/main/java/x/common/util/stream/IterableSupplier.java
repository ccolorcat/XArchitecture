package x.common.util.stream;

import androidx.annotation.NonNull;

import java.util.Iterator;

/**
 * Author: cxx
 * Date: 2020-05-22
 * GitHub: https://github.com/ccolorcat
 */
public final class IterableSupplier<T> extends BaseSupplier<T> {
    private final Iterator<? extends T> iterator;

    public IterableSupplier(@NonNull Iterable<? extends T> iterator) {
        this.iterator = iterator.iterator();
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
}
