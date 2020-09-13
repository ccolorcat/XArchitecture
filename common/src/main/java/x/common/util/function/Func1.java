package x.common.util.function;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
public interface Func1<T, R> extends Func {
    R apply(T t) throws Throwable;
}
