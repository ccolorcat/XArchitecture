package x.common.util.function;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public interface Producer<T> extends ThrowableProducer<T> {
    @Override
    T produce();
}
