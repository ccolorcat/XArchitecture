package x.common.util.stream;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-05-25
 * GitHub: https://github.com/ccolorcat
 */
final class MergeNode<T> extends Node<T, T> {
    private final List<? extends T> sorted;
    private final Comparator<? super T> comparator;
    private int index = 0;

    MergeNode(@NonNull List<? extends T> sorted, @NonNull Comparator<? super T> comparator) {
        this.sorted = Utils.requireNonNull(sorted);
        this.comparator = Utils.requireNonNull(comparator);
    }

    @Override
    void accept(T t) throws Throwable {
        final int size = sorted.size();
        if (index >= size) {
            next.accept(t);
            return;
        }
        T user;
        for (; index < size && !next.shouldEnd(); ++index) {
            user = sorted.get(index);
            if (comparator.compare(user, t) <= 0) {
                next.accept(user);
            } else {
                break;
            }
        }
        next.accept(t);
    }

    @Override
    void end() {
        if (index < sorted.size()) {
            FakeStream.from(sorted.subList(index, sorted.size()))
                    .terminate(new AdapterNode<>(next, Ops.SHOULD_END | Ops.ACCEPT | Ops.END));
        } else {
            super.end();
        }
    }
}
