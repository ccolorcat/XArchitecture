package x.common.util.stream;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
class SortedNode<In> extends Node<In, In> {
    private List<In> sorted;
    private final Comparator<? super In> comparator;

    SortedNode(Comparator<? super In> comparator) {
        this.comparator = comparator;
    }

    @Override
    void begin(long size) {
        sorted = new LinkedList<>();
    }

    @Override
    void end() {
        Collections.sort(sorted, comparator);
        FakeStream.from(sorted).terminate(new AdapterNode<>(next, Ops.ALL));
    }

    @Override
    boolean shouldEnd() {
        return false;
    }

    @Override
    void accept(In in) {
        sorted.add(in);
    }
}
