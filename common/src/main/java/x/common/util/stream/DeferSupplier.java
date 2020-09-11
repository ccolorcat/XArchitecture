package x.common.util.stream;

import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-26
 * GitHub: https://github.com/ccolorcat
 */
public class DeferSupplier<T> extends BaseSupplier<T> {
    private final Func0<? extends T> producer;

    public DeferSupplier(Func0<? extends T> producer) {
        this.producer = producer;
    }

    @Override
    public boolean hasNext() throws IOException {
        super.hasNext();
        return true;
    }

    @Override
    public T next() throws IOException {
        super.next();
        return producer.apply();
    }
}
