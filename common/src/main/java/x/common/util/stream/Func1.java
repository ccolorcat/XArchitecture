package x.common.util.stream;

import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
public interface Func1<T, R> extends Function {
    R apply(T t) throws IOException;
}
