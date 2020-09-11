package x.common.util.stream;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
final class SortedNode<In> extends Node<In, In> {
    private List<In> sorted;
    private final Comparator<? super In> comparator;

    SortedNode(@NonNull Comparator<? super In> comparator) {
        this.comparator = Utils.requireNonNull(comparator);
    }

    @Override
    void begin(long size) {
        sorted = new LinkedList<>();
    }

    @Override
    boolean shouldEnd() {
        return false;
    }

    @Override
    void accept(In in) throws Throwable {
        sorted.add(in);
    }

    @Override
    void end() {
        Collections.sort(sorted, comparator);
        FakeStream.from(sorted).terminate(new AdapterNode<>(next, Ops.ALL));
    }
}
