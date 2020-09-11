package x.common.util.stream;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public class CollectionSupplier<T> extends BaseSupplier<T> {
    private final long size;
    private final Iterator<T> iterator;

    public CollectionSupplier(Collection<T> collection) {
        size = collection.size();
        iterator = collection.iterator();
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

    @Override
    public long size() {
        return size;
    }
}
