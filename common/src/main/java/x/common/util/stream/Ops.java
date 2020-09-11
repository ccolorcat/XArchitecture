package x.common.util.stream;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public interface Ops {
    int NON = 0;
    int BEGIN = 1;
    int ACCEPT = 1 << 1;
    int SHOULD_END = 1 << 2;
    int END = 1 << 3;
    int ALL = BEGIN | ACCEPT | SHOULD_END | END;
}
