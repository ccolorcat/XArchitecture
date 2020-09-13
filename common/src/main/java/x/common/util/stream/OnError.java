package x.common.util.stream;

import x.common.util.function.Action1;

/**
 * Author: cxx
 * Date: 2020-05-25
 * GitHub: https://github.com/ccolorcat
 */
public interface OnError extends Action1<Throwable> {
    @Override
    void call(Throwable e);
}
