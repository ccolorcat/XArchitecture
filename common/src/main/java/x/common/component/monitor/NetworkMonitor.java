package x.common.component.monitor;

import x.common.component.XObservable;
import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-08-05
 * GitHub: https://github.com/ccolorcat
 */
@Core(NetworkMonitorCompat.class)
public interface NetworkMonitor extends XObservable<Boolean> {
    boolean isAvailable();
}
