package x.common.util.function;

/**
 * Author: cxx
 * Date: 2020-09-07
 * GitHub: https://github.com/ccolorcat
 */
public interface ThrowableProducer<T> {
    T produce() throws Throwable;
}
