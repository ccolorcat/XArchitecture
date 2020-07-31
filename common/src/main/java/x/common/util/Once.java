package x.common.util;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-20
 * GitHub: https://github.com/ccolorcat
 */
public final class Once<T> {
    private T value;

    public Once() {}

    public Once(@NonNull T value) {
        this.value = Utils.requireNonNull(value, "value == null");
    }

    public void set(@NonNull T value) {
        if (this.value != null) throw new IllegalStateException("the value already set");
        this.value = Utils.requireNonNull(value, "value == null");
    }

    @NonNull
    public T get() {
        if (this.value == null) throw new IllegalStateException("the value not set");
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Once{" +
                "value=" + value +
                '}';
    }
}
