package x.common.util.stream;

/**
 * Author: cxx
 * Date: 2020-05-20
 * GitHub: https://github.com/ccolorcat
 */
abstract class TerminalNode<In, Out> extends Node<In, Out> {
    @Override
    void begin(long size) {
        System.out.println(getClass() + " begin " + size);
    }

    @Override
    boolean shouldEnd() {
        return result != null;
    }

    @Override
    void end() {
        System.out.println(getClass() + " end");
    }
}
