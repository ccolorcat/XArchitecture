package x.common.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
public final class Utils {
    public static <T> T nullElse(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T extends CharSequence> T emptyElse(T text, T defaultValue) {
        return isEmpty(text) ? defaultValue : text;
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence text) {
        return !isEmpty(text);
    }

    @NonNull
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) throw new NullPointerException(message);
        return obj;
    }

    @NonNull
    public static <T> T requireNonNull(T obj) {
        if (obj == null) throw new NullPointerException();
        return obj;
    }

    @Nullable
    public static <T> Constructor<T> quietGetConstructor(@NonNull Class<T> tClass, Class<?>... parameterTypes) {
        try {
            return tClass.getDeclaredConstructor(parameterTypes);
        } catch (Throwable t) {
            return null;
        }
    }

    public static void sleep(@NonNull TimeUnit unit, long duration) {
        sleep(unit.toMillis(duration));
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable ignore) {
        }
    }

    private Utils() {
        throw new AssertionError("no instance");
    }
}
