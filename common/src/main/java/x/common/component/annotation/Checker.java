package x.common.component.annotation;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
final class Checker {
    static boolean assertImpl(@NonNull Class<?> father, @NonNull Class<?> child) {
        if (!father.isAssignableFrom(child)) {
            throw new IllegalArgumentException(child + " must extends or implements " + father);
        }
        return true;
    }

    private Checker() {
        throw new AssertionError("no instance");
    }
}
