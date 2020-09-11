package x.common.util.stream;

import androidx.annotation.CallSuper;

import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public abstract class BaseSupplier<T> implements Supplier<T> {
    private boolean closed = false;

    @CallSuper
    @Override
    public boolean hasNext() throws Throwable {
        checkStatus();
        return false;
    }

    @CallSuper
    @Override
    public T next() throws Throwable {
        checkStatus();
        return null;
    }

    @Override
    public long size() {
        return -1L;
    }

    @CallSuper
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
