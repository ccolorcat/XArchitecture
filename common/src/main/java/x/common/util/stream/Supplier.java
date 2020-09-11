package x.common.util.stream;

import java.io.Closeable;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public interface Supplier<T> extends Closeable {
    boolean hasNext() throws Throwable;

    T next() throws Throwable;

    long size();
}
