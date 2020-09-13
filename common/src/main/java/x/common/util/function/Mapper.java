package x.common.util.function;

/**
 * Author: cxx
 * Date: 2020-09-12
 * GitHub: https://github.com/ccolorcat
 */
public interface Mapper<T, R> extends ThrowableMapper<T, R> {
    @Override
    R apply(T t);
}
