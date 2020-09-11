package x.common.util.stream;

import androidx.annotation.NonNull;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-05-25
 * GitHub: https://github.com/ccolorcat
 */
final class ConcatNode<T> extends Node<T, T> {
    private final FakeStream<? extends T> stream;

    ConcatNode(@NonNull FakeStream<? extends T> stream) {
        this.stream = Utils.requireNonNull(stream);
    }

    @Override
    void begin(long size) {
        long streamSize = stream.head.supplier.size();
        long newSize = size < 0 || streamSize < 0 ? -1L : (Long.MAX_VALUE - size > streamSize ? size + streamSize : Long.MAX_VALUE);
        super.begin(newSize);
    }

    @Override
    void accept(T t) throws Throwable {
        next.accept(t);
    }

    @Override
    void end() {
        stream.terminate(new AdapterNode<>(next, Ops.SHOULD_END | Ops.ACCEPT | Ops.END));
    }
}
