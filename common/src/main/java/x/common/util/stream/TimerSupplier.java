package x.common.util.stream;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Author: cxx
 * Date: 2020-05-26
 * GitHub: https://github.com/ccolorcat
 */
public class TimerSupplier<T> extends BaseSupplier<T> {
    private final Supplier<? extends T> supplier;
    private final long interval; // millis

    public TimerSupplier(Supplier<? extends T> supplier, TimeUnit unit, long interval) {
        this.supplier = supplier;
        this.interval = unit.toMillis(interval);
    }

    @Override
    public boolean hasNext() throws IOException {
        super.hasNext();
        return supplier.hasNext();
    }

    @Override
    public T next() throws IOException {
        super.next();
        try {
            Thread.sleep(interval);
        } catch (InterruptedException ignore) {
        }
        return supplier.next();
    }

    @Override
    public long size() {
        return supplier.size();
    }

    @Override
    public void close() throws IOException {
        supplier.close();
        super.close();
    }
}
