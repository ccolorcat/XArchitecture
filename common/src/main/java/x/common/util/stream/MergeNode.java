package x.common.util.stream;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Author: cxx
 * Date: 2020-05-25
 * GitHub: https://github.com/ccolorcat
 */
final class MergeNode<T> extends Node<T, T> {
    private final List<? extends T> sorted;
    private final Comparator<? super T> comparator;
    private int index = 0;

    MergeNode(List<? extends T> sorted, Comparator<? super T> comparator) {
        this.sorted = sorted;
        this.comparator = comparator;
    }

    @Override
    void accept(T t) throws IOException {
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
