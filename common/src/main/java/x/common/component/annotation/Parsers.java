package x.common.component.annotation;

import androidx.annotation.NonNull;

import x.common.component.HummingbirdException;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
final class Parsers {
    static boolean assertImpl(@NonNull Class<?> father, @NonNull Class<?> child) throws Throwable {
        if (!father.isAssignableFrom(child)) {
            throw new HummingbirdException(child + " must extends or implements " + father);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    static <T> Class<? extends T> requireImpl(Class<T> father, Class<?> child, String implClassName) throws Throwable {
        Class<?> impl = child;
        if (impl == Void.class) {
            impl = Utils.isEmpty(implClassName) ? father : Class.forName(implClassName);
        }
        Parsers.assertImpl(father, impl);
        return (Class<? extends T>) impl;
    }

    private Parsers() {
        throw new AssertionError("no instance");
    }
}
