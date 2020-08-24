package x.common.util.function;

/**
 * Author: cxx
 * Date: 2020-08-23
 * GitHub: https://github.com/ccolorcat
 */
public interface ThrowableConsumer<T> {
    void consume(T t) throws Throwable;
}
