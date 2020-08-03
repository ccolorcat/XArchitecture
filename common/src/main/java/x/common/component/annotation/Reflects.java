package x.common.component.annotation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;

/**
 * Author: cxx
 * Date: 2020-08-03
 * GitHub: https://github.com/ccolorcat
 */
public final class Reflects {
    @Nullable
    public static <T> Constructor<T> quietGetConstructor(@NonNull Class<T> tClass, Class<?>... parameterTypes) {
        try {
            return tClass.getDeclaredConstructor(parameterTypes);
        } catch (Throwable t) {
            return null;
        }
    }

    @NonNull
    public static <T> T newDefaultInstance(@NonNull Class<T> tClass) throws Throwable {
        Constructor<T> constructor = tClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private Reflects() {
        throw new AssertionError("no instance");
    }
}
