package x.common.util.stream;

/**
 * Author: cxx
 * Date: 2020-05-25
 * GitHub: https://github.com/ccolorcat
 */
public interface OnError extends Action1<Throwable> {
    @Override
    void call(Throwable e);
}
