package x.common.util.stream;

import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public abstract class BaseSupplier<T> implements Supplier<T> {
    private boolean closed = false;

    @Override
    public boolean hasNext() throws IOException {
        checkStatus();
        return false;
    }

    @Override
    public T next() throws IOException {
        checkStatus();
        return null;
    }

    @Override
    public long size() {
        return -1L;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    protected final void checkStatus() {
        if (closed) {
            throw new IllegalStateException("closed");
        }
    }
}
