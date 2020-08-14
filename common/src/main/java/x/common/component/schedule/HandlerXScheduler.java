package x.common.component.schedule;

/**
 * Author: cxx
 * Date: 2020-08-03
 * GitHub: https://github.com/ccolorcat
 */
public interface HandlerXScheduler extends XScheduler {
    void removeAll();

    boolean has(Runnable task);
}
