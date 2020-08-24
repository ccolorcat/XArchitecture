package x.common.component;

import androidx.annotation.NonNull;

import x.common.util.Utils;
import x.common.util.function.Producer;


/**
 * Thread-safe
 * <p>
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public final class Lazy<T> {
    public static <T> Lazy<T> by(@NonNull Producer<? extends T> producer) {
        return new Lazy<>(producer);
    }

    private final Producer<? extends T> producer;
    private volatile T value;

    private Lazy(@NonNull Producer<? extends T> producer) {
        this.producer = Utils.requireNonNull(producer, "producer == null");
    }

    public boolean initialized() {
        return value != null;
    }

    @NonNull
    public T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    value = Utils.requireNonNull(producer.produce(), "producer return's null");
                }
            }
        }
        return value;
    }
}
