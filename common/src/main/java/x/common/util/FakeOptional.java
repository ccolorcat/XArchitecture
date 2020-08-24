package x.common.util;

import androidx.annotation.NonNull;

import java.util.NoSuchElementException;

import x.common.util.function.Consumer;
import x.common.util.function.Producer;
import x.common.util.function.ThrowableConsumer;

@SuppressWarnings({"unused", "EqualsReplaceableByObjectsCall"})
public final class FakeOptional<T> {
    private static final FakeOptional<?> EMPTY = new FakeOptional<>();

    @NonNull
    public static <T> FakeOptional<T> empty() {
        @SuppressWarnings("unchecked")
        FakeOptional<T> t = (FakeOptional<T>) EMPTY;
        return t;
    }

    @NonNull
    public static <T> FakeOptional<T> of(@NonNull T value) {
        return new FakeOptional<>(value);
    }

    @NonNull
    public static <T> FakeOptional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    private final T value;

    private FakeOptional() {
        this.value = null;
    }

    private FakeOptional(T value) {
        this.value = Utils.requireNonNull(value, "value == null");
    }

    @NonNull
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(@NonNull Consumer<? super T> consumer) {
        if (value != null)
            consumer.consume(value);
    }

    public boolean safeIfPresent(@NonNull ThrowableConsumer<? super T> consumer) {
        if (value != null) {
            try {
                consumer.consume(value);
                return true;
            } catch (Throwable ignore) {
            }
        }
        return false;
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(@NonNull Producer<? extends T> other) {
        return value != null ? value : other.produce();
    }

    @NonNull
    public <X extends Throwable> T orElseThrow(@NonNull Producer<? extends X> exceptionProducer) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionProducer.produce();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FakeOptional<?> optional = (FakeOptional<?>) o;

        return value != null ? value.equals(optional.value) : optional.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value != null
                ? String.format("Optional[%s]", value)
                : "Optional.empty";
    }
}
