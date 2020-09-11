package x.common.util.stream;

import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
public interface Action1<T> extends Action {
    void call(T t) throws IOException;
}
