package x.common.util.function;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
public interface Consumer<T> extends ThrowableConsumer<T> {
    @Override
    void call(T t);
}
