package x.common.util.stream;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import x.common.util.Utils;
import x.common.util.function.Action1;
import x.common.util.function.Func0;
import x.common.util.function.Func1;
import x.common.util.function.Func2;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
//@SuppressWarnings("unused")
public final class FakeStream<T> {
    @SafeVarargs
    @NonNull
    public static <T> FakeStream<T> of(T t1, T t2, T... more) {
        List<T> ts = new ArrayList<>(more.length + 2);
        ts.add(t1);
        ts.add(t2);
        if (more.length > 0) {
            ts.addAll(Arrays.asList(more));
        }
        return from(ts);
    }

    @NonNull
    public static <T> FakeStream<T> of(T[] array) {
        return from(Arrays.asList(array));
    }

    @NonNull
    public static <T> FakeStream<T> just(T t) {
        return from(Collections.singletonList(t));
    }

    @NonNull
    public static <T> FakeStream<T> defer(@NonNull Func0<? extends T> producer) {
        return generate(new DeferSupplier<>(producer));
    }

    @NonNull
    public static <T> FakeStream<T> timer(@NonNull TimeUnit unit, long interval, @NonNull Func0<? extends T> producer) {
        return timer(unit, interval, new DeferSupplier<>(producer));
    }

    @NonNull
    public static <T> FakeStream<T> timer(@NonNull TimeUnit unit, long interval, @NonNull Supplier<? extends T> supplier) {
        return generate(new TimerSupplier<>(supplier, unit, interval));
    }

    @NonNull
    public static <T> FakeStream<T> from(@NonNull Collection<? extends T> data) {
        return generate(new CollectionSupplier<>(data));
    }

    @NonNull
    public static FakeStream<String> from(@NonNull LineNumberReader reader) {
        return generate(new LineReaderSupplier(reader));
    }

    @NonNull
    public static FakeStream<char[]> from(@NonNull Reader reader) {
        return generate(new CharReaderSupplier(reader));
    }

    @NonNull
    public static FakeStream<byte[]> from(@NonNull InputStream input) {
        return generate(new ByteSupplier(input));
    }

    @NonNull
    public static <T> FakeStream<T> from(@NonNull Iterable<? extends T> iterable) {
        return generate(new IterableSupplier<>(iterable));
    }

    @NonNull
    public static <T> FakeStream<T> generate(@NonNull Supplier<? extends T> supplier) {
        return new FakeStream<>(new HeadNode<>(supplier));
    }


    final HeadNode<?> head;
    private final Node<?, ? extends T> last;

    private FakeStream(@NonNull HeadNode<T> head) {
        this.head = head;
        this.last = head;
    }

    private FakeStream(@NonNull HeadNode<?> head, @NonNull Node<?, ? extends T> last) {
        this.head = head;
        this.last = last;
    }

    private FakeStream(@NonNull HeadNode<?> head, @NonNull TerminalNode<?, ? extends T> last) {
        this.head = head;
        this.last = last;
        this.head.emit();
    }

    @NonNull
    public FakeStream<T> onBegin(OnBegin onBegin) {
        head.onBegin = onBegin;
        return this;
    }

    @NonNull
    public FakeStream<T> onError(OnError onError) {
        head.onError = onError;
        return this;
    }

    @NonNull
    public FakeStream<T> onEnd(OnEnd onEnd) {
        head.onEnd = onEnd;
        return this;
    }

    T result() {
        return last.result;
    }

    @NonNull
    <R> FakeStream<R> next(@NonNull Node<? super T, ? extends R> next) {
        last.next = Utils.requireNonNull(next);
        return new FakeStream<>(head, next);
    }

    @NonNull
    <R> FakeStream<R> terminate(@NonNull TerminalNode<? super T, ? extends R> terminal) {
        last.next = Utils.requireNonNull(terminal);
        return new FakeStream<>(head, terminal);
    }

    @NonNull
    public FakeStream<T> filter(@NonNull final Func1<? super T, Boolean> filter) {
        Utils.requireNonNull(filter);
        return next(new Node<T, T>() {
            @Override
            void accept(T t) throws Throwable {
                if (filter.apply(t)) {
                    next.accept(t);
                }
            }
        });
    }

    @NonNull
    public FakeStream<T> skipNull() {
        return filter(new Func1<T, Boolean>() {
            @Override
            public Boolean apply(T t) throws Throwable {
                return t != null;
            }
        });
    }

    @NonNull
    public FakeStream<T> skip(final int count) {
        return filter(new Func1<T, Boolean>() {
            private int skipCount = 0;

            @Override
            public Boolean apply(T t) throws Throwable {
                return ++skipCount > count;
            }
        });
    }

    @NonNull
    public FakeStream<T> sort(@NonNull Comparator<? super T> comparator) {
        return next(new SortedNode<>(comparator));
    }

    @NonNull
    public FakeStream<T> peek(@NonNull final Action1<? super T> peek) {
        Utils.requireNonNull(peek);
        return next(new Node<T, T>() {
            @Override
            void accept(T t) throws Throwable {
                peek.call(t);
                next.accept(t);
            }
        });
    }

    @NonNull
    public <R> FakeStream<R> map(@NonNull final Func1<? super T, ? extends R> mapper) {
        Utils.requireNonNull(mapper);
        return next(new Node<T, R>() {
            @Override
            void accept(T t) throws Throwable {
                next.accept(mapper.apply(t));
            }
        });
    }

    @NonNull
    public <R> FakeStream<R> flatMap(@NonNull final Func1<? super T, ? extends FakeStream<? extends R>> mapper) {
        Utils.requireNonNull(mapper);
        return next(new Node<T, R>() {
            private AdapterNode<R> adapter;

            @Override
            void accept(T t) throws Throwable {
                if (adapter == null) {
                    adapter = new AdapterNode<>(next, Ops.SHOULD_END | Ops.ACCEPT);
                }
                if (!adapter.shouldEnd()) {
                    mapper.apply(t).terminate(adapter);
                }
            }
        });
    }

    @NonNull
    public <R, E> FakeStream<R> zip(@NonNull final List<? extends E> es, @NonNull final Func2<? super T, ? super E, ? extends R> zipper) {
        Utils.requireNonNull(es);
        Utils.requireNonNull(zipper);
        return next(new Node<T, R>() {
            private int index = 0;

            @Override
            boolean shouldEnd() {
                return index >= es.size() || next.shouldEnd();
            }

            @Override
            void accept(T t) throws Throwable {
                next.accept(zipper.apply(t, es.get(index++)));
            }
        });
    }

    @NonNull
    public FakeStream<T> concat(@NonNull final Collection<? extends T> ts) {
        return concat(FakeStream.from(ts));
    }

    @NonNull
    public FakeStream<T> concat(@NonNull final FakeStream<? extends T> stream) {
        return next(new ConcatNode<>(stream));
    }

    @NonNull
    public FakeStream<T> merge(@NonNull final List<? extends T> sorted, @NonNull final Comparator<? super T> comparator) {
        return next(new MergeNode<>(sorted, comparator));
    }

    public void forEach(@NonNull final Action1<? super T> consumer) {
        Utils.requireNonNull(consumer);
        terminate(new TerminalNode<T, Void>() {
            @Override
            void accept(T t) throws Throwable {
                consumer.call(t);
            }
        });
    }

    public T findFirst(@NonNull final Func1<? super T, Boolean> finder) {
        Utils.requireNonNull(finder);
        return terminate(new TerminalNode<T, T>() {
            @Override
            void accept(T t) throws Throwable {
                if (finder.apply(t)) {
                    result = t;
                }
            }
        }).result();
    }

    public T findLast(@NonNull final Func1<? super T, Boolean> finder) {
        Utils.requireNonNull(finder);
        return terminate(new TerminalNode<T, T>() {
            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return false;
            }

            @Override
            void accept(T t) throws Throwable {
                if (finder.apply(t)) {
                    result = t;
                }
            }
        }).result();
    }

    public T max(@NonNull final Comparator<? super T> comparator) {
        Utils.requireNonNull(comparator);
        return terminate(new TerminalNode<T, T>() {
            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return false;
            }

            @Override
            void accept(T t) throws Throwable {
                if (result == null || comparator.compare(t, result) > 0) {
                    result = t;
                }
            }
        }).result();
    }

    public T min(@NonNull final Comparator<? super T> comparator) {
        Utils.requireNonNull(comparator);
        return max(new Comparator<T>() {
            @Override
            public int compare(T t, T t2) {
                return -comparator.compare(t, t2);
            }
        });
    }

    public boolean matchAny(@NonNull final Func1<? super T, Boolean> matcher) {
        Utils.requireNonNull(matcher);
        return terminate(new TerminalNode<T, Boolean>() {
            @Override
            void begin(long size) {
                super.begin(size);
                result = false;
            }


            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return result;
            }

            @Override
            void accept(T t) throws Throwable {
                result = matcher.apply(t);
            }
        }).result();
    }

    public boolean matchAll(@NonNull final Func1<? super T, Boolean> matcher) {
        Utils.requireNonNull(matcher);
        return terminate(new TerminalNode<T, Boolean>() {
            @Override
            void begin(long size) {
                super.begin(size);
                result = true;
            }

            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return !result;
            }

            @Override
            void accept(T t) throws Throwable {
                result = matcher.apply(t);
            }
        }).result();
    }

    public <K> Map<K, List<T>> group(@NonNull final Func1<? super T, ? extends K> keyGenerator) {
        Utils.requireNonNull(keyGenerator);
        return terminate(new TerminalNode<T, Map<K, List<T>>>() {
            @Override
            void begin(long size) {
                super.begin(size);
                result = new HashMap<>();
            }

            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return false;
            }

            @Override
            void accept(T t) throws Throwable {
                K key = keyGenerator.apply(t);
                List<T> value = result.get(key);
                if (value == null) {
                    value = new ArrayList<>();
                    result.put(key, value);
                }
                value.add(t);
            }
        }).result();
    }

    public List<T> take(final int count) {
        return terminate(new TerminalNode<T, List<T>>() {
            @Override
            void begin(long size) {
                super.begin(size);
                result = new ArrayList<>(computeMinCapacity(size, count));
            }

            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return result.size() >= count;
            }

            @Override
            void accept(T t) throws Throwable {
                result.add(t);
            }
        }).result();
    }

    public List<T> takeAll() {
        return terminate(new TerminalNode<T, List<T>>() {
            @Override
            void begin(long size) {
                super.begin(size);
                result = new ArrayList<>(computeMinCapacity(size, Integer.MAX_VALUE));
            }

            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return false;
            }

            @Override
            void accept(T t) throws Throwable {
                result.add(t);
            }
        }).result();
    }

    public int count() {
        return terminate(new TerminalNode<T, Integer>() {
            @Override
            void begin(long size) {
                super.begin(size);
                result = 0;
            }

            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return false;
            }

            @Override
            void accept(T t) throws Throwable {
                ++result;
            }
        }).result();
    }

    public T compute(@NonNull final Func2<? super T, ? super T, ? extends T> computer) {
        Utils.requireNonNull(computer);
        return terminate(new TerminalNode<T, T>() {
            @Override
            boolean shouldEnd() {
                super.shouldEnd();
                return false;
            }

            @Override
            void accept(T t) throws Throwable {
                result = result == null ? t : computer.apply(result, t);
            }
        }).result();
    }
}
