package x.common.util.stream;

import androidx.annotation.NonNull;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-05-22
 * GitHub: https://github.com/ccolorcat
 */
final class AdapterNode<R> extends TerminalNode<R, R> {
    private final int ops;

    AdapterNode(@NonNull Node<? super R, ?> next, int ops) {
        this.next = Utils.requireNonNull(next);
        this.ops = ops;
    }

    @Override
    void begin(long size) {
        if ((Ops.BEGIN & ops) != 0) {
            next.begin(size);
        }
    }

    @Override
    boolean shouldEnd() {
        if ((Ops.SHOULD_END & ops) != 0) {
            return next.shouldEnd();
        }
        return super.shouldEnd();
    }

    @Override
    void accept(R r) throws Throwable {
        if ((Ops.ACCEPT & ops) != 0) {
            next.accept(r);
        }
    }

    @Override
    void end() {
        if ((Ops.END & ops) != 0) {
            next.end();
        }
    }
}
