package x.common.component;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("UnusedReturnValue")
public interface Registrable<T> {
    boolean register(T t);

    boolean unregister(T t);

    default AutoRegistrable<T> asAutoRegistrable() {
        if (this instanceof AutoRegistrable) return (AutoRegistrable<T>) this;
        return null;
    }
}
