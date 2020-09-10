package x.common.component.core;

import androidx.lifecycle.LifecycleEventObserver;

import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-09-09
 * GitHub: https://github.com/ccolorcat
 */
@Core(PageTrackerImpl.class)
public interface PageTracker extends LifecycleEventObserver {
}
