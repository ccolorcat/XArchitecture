package x.common.util.stream;

import java.io.Closeable;
import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public interface Supplier<T> extends Closeable {
    boolean hasNext() throws IOException;

    T next() throws IOException;

    long size();
}
