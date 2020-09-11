package x.common.util.stream;

import androidx.annotation.NonNull;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-05-26
 * GitHub: https://github.com/ccolorcat
 */
public final class DeferSupplier<T> extends BaseSupplier<T> {
    private final Func0<? extends T> producer;

    public DeferSupplier(@NonNull Func0<? extends T> producer) {
        this.producer = Utils.requireNonNull(producer, "producer == null");
    }

    @Override
    public boolean hasNext() throws Throwable {
        super.hasNext();
        return true;
    }

    @Override
    public T next() throws Throwable {
        super.next();
        return producer.apply();
    }
}
