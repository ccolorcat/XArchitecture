package x.common.util.stream;

import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
public interface Func2<T1, T2, R> extends Function {
    R apply(T1 t1, T2 t2) throws IOException;
}
