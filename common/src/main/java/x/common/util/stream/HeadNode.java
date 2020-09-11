package x.common.util.stream;

import java.io.Closeable;
import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
class HeadNode<T> extends Node<T, T> {
    final Supplier<? extends T> supplier;
    OnBegin onBegin;
    OnError onError;
    OnEnd onEnd;

    HeadNode(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    @Override
    void begin(long size) {
        if (onBegin != null) onBegin.call();
        super.begin(size);
    }

    @Override
    void accept(T t) throws Throwable {
        next.accept(t);
    }

    @Override
    void end() {
        super.end();
        if (onEnd != null) onEnd.call();
    }

    void emit() {
        try {
            begin(supplier.size());
            while (supplier.hasNext() && !shouldEnd()) {
                accept(supplier.next());
            }
        } catch (Throwable e) {
            if (onError != null) onError.call(e);
        } finally {
            close(supplier);
            end();
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {

            }
        }
    }
}
