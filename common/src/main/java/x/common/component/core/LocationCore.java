package x.common.component.core;

import x.common.component.XObservable;
import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-09-14
 * GitHub: https://github.com/ccolorcat
 */
@Core(LocationCoreImpl.class)
public interface LocationCore extends XObservable<XLocation> {
    void start();

    void requestLocation();

    void stop();
}
