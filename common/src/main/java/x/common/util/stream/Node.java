package x.common.util.stream;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
abstract class Node<In, Out> {
    Node<? super Out, ?> next;
    Out result;


    void begin(long size) {
        next.begin(size);
    }

    boolean shouldEnd() {
        return next.shouldEnd();
    }

    abstract void accept(In in) throws Throwable;

    void end() {
        next.end();
    }

    static int computeMinCapacity(long beginSize, int expectCapacity) {
        return (int) Math.max(Math.min(beginSize, expectCapacity), 0);
    }
}
