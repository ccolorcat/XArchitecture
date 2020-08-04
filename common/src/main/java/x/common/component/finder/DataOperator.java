package x.common.component.finder;

/**
 * Author: cxx
 * Date: 2020-04-15
 */
public interface DataOperator {
    boolean exists();

    long size();

    void delete();
}
