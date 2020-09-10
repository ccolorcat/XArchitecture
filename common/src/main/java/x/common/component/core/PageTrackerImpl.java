package x.common.component.core;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-09-09
 * GitHub: https://github.com/ccolorcat
 */
final class PageTrackerImpl implements PageTracker {

    PageTrackerImpl() {
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        Logger.getLogger("PageTracker").v(source.getClass() + "#onStateChanged#" + event);
    }
}
