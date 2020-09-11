package x.common.util.stream;

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

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("unused")
public class FakeStream<T> {
    @SafeVarargs
    public static <T> FakeStream<T> of(T t1, T t2, T... more) {
        List<T> ts = new ArrayList<>(more.length + 2);
        ts.add(t1);
        ts.add(t2);
        if (more.length > 0) {
            ts.addAll(Arrays.asList(more));
        }
        return from(ts);
    }

    public static <T> FakeStream<T> of(T[] array) {
        return from(Arrays.asList(array));
    }

    public static <T> FakeStream<T> just(T t) {
        return from(Collections.singletonList(t));
    }

    public static <T> FakeStream<T> defer(final Func0<? extends T> producer) {
        return generate(new DeferSupplier<>(producer));
    }

    public static <T> FakeStream<T> timer(TimeUnit unit, long interval, Func0<? extends T> producer) {
        return timer(unit, interval, new DeferSupplier<>(producer));
    }

    public static <T> FakeStream<T> timer(TimeUnit unit, long interval, Supplier<? extends T> supplier) {
        return generate(new TimerSupplier<>(supplier, unit, interval));
    }

    public static <T> FakeStream<T> from(Collection<? extends T> data) {
        return generate(new CollectionSupplier<>(data));
    }

    public static FakeStream<String> from(LineNumberReader reader) {
        return generate(new LineReaderSupplier(reader));
    }

    public static FakeStream<char[]> from(Reader reader) {
        return generate(new CharReaderSupplier(reader));
    }

    public static FakeStream<byte[]> from(InputStream input) {
        return generate(new ByteSupplier(input));
    }

    public static <T> FakeStream<T> from(Iterable<? extends T> iterable) {
        return generate(new IterableSupplier<>(iterable));
    }

    public static <T> FakeStream<T> generate(Supplier<? extends T> supplier) {
        return new FakeStream<>(new HeadNode<>(supplier));
    }


    final HeadNode<?> head;
    private final Node<?, ? extends T> last;

    private FakeStream(HeadNode<T> head) {
        this.head = head;
        this.last = head;
    }

    private FakeStream(HeadNode<?> head, Node<?, ? extends T> last) {
        this.head = head;
        this.last = last;
    }

    private FakeStream(HeadNode<?> head, TerminalNode<?, ? extends T> last) {
        this.head = head;
        this.last = last;
        this.head.emit();
    }

    public FakeStream<T> onBegin(OnBegin onBegin) {
        head.onBegin = onBegin;
        return this;
    }

    public FakeStream<T> onError(OnError onError) {
        head.onError = onError;
        return this;
    }

    public FakeStream<T> onEnd(OnEnd onEnd) {
        head.onEnd = onEnd;
        return this;
    }

    T result() {
        return last.result;
    }


    <R> FakeStream<R> next(Node<? super T, ? extends R> next) {
        last.next = next;
        return new FakeStream<>(head, next);
    }

    <R> FakeStream<R> terminate(TerminalNode<? super T, ? extends R> terminal) {
        last.next = terminal;
        return new FakeStream<>(head, terminal);
    }

    public FakeStream<T> filter(final Func1<? super T, Boolean> filter) {
        return next(new Node<T, T>() {
            @Override
            void accept(T t) throws Throwable {
                if (filter.apply(t)) {
                    next.accept(t);
                }
            }
        });
    }

    public FakeStream<T> skipNull() {
        return filter(new Func1<T, Boolean>() {
            @Override
            public Boolean apply(T t) {
                return t != null;
            }
        });
    }

    public FakeStream<T> skip(final int count) {
        return filter(new Func1<T, Boolean>() {
            private int skipCount = 0;

            @Override
            public Boolean apply(T t) {
                return ++skipCount > count;
            }
        });
    }

    public FakeStream<T> sort(Comparator<? super T> comparator) {
        return next(new SortedNode<>(comparator));
    }

    public FakeStream<T> peek(final Action1<? super T> peek) {
        return next(new Node<T, T>() {
            @Override
            void accept(T t) throws Throwable {
                peek.call(t);
                next.accept(t);
            }
        });
    }

    public <R> FakeStream<R> map(final Func1<? super T, ? extends R> mapper) {
        return next(new Node<T, R>() {
            @Override
            void accept(T t) throws Throwable {
                next.accept(mapper.apply(t));
            }
        });
    }

    public <R> FakeStream<R> flatMap(final Func1<? super T, ? extends FakeStream<? extends R>> mapper) {
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

    public <R, E> FakeStream<R> zip(final List<? extends E> es, final Func2<? super T, ? super E, ? extends R> zipper) {
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

    public FakeStream<T> concat(final Collection<? extends T> ts) {
        return concat(FakeStream.from(ts));
    }

    public FakeStream<T> concat(final FakeStream<? extends T> stream) {
        return next(new ConcatNode<>(stream));
    }

    public FakeStream<T> merge(final List<? extends T> sorted, final Comparator<? super T> comparator) {
        return next(new MergeNode<>(sorted, comparator));
    }


    public void forEach(final Action1<? super T> consumer) {
        terminate(new TerminalNode<T, Void>() {
            @Override
            void accept(T t) throws Throwable {
                consumer.call(t);
            }
        });
    }

    public T findFirst(final Func1<? super T, Boolean> finder) {
        return terminate(new TerminalNode<T, T>() {
            @Override
            void accept(T t) throws Throwable {
                if (finder.apply(t)) {
                    result = t;
                }
            }
        }).result();
    }

    public T findLast(final Func1<? super T, Boolean> finder) {
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

    public T max(final Comparator<? super T> comparator) {
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

    public T min(final Comparator<? super T> comparator) {
        return max(new Comparator<T>() {
            @Override
            public int compare(T t, T t2) {
                return -comparator.compare(t, t2);
            }
        });
    }

    public boolean matchAny(final Func1<? super T, Boolean> matcher) {
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

    public boolean matchAll(final Func1<? super T, Boolean> matcher) {
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

    public <K> Map<K, List<T>> group(final Func1<? super T, ? extends K> keyGenerator) {
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

    public T compute(final Func2<? super T, ? super T, ? extends T> computer) {
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
