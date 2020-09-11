package x.common.util.stream;

import java.io.IOException;
import java.util.Iterator;

/**
 * Author: cxx
 * Date: 2020-05-22
 * GitHub: https://github.com/ccolorcat
 */
public class IterableSupplier<T> extends BaseSupplier<T> {
    private final Iterator<? extends T> iterator;

    public IterableSupplier(Iterable<? extends T> iterator) {
        this.iterator = iterator.iterator();
    }

    @Override
    public boolean hasNext() throws IOException {
        super.hasNext();
        return iterator.hasNext();
    }

    @Override
    public T next() throws IOException {
        super.next();
        return iterator.next();
    }
}
