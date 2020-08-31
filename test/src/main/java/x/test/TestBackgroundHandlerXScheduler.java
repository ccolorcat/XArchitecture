package x.test;

import x.common.component.schedule.BackgroundHandlerXScheduler;
import x.common.component.schedule.SchedulerWrapper;

/**
 * Author: cxx
 * Date: 2020-08-07
 * GitHub: https://github.com/ccolorcat
 */
class TestBackgroundHandlerXScheduler extends SchedulerWrapper implements BackgroundHandlerXScheduler {
    protected TestBackgroundHandlerXScheduler() {
        super(1);
    }

    @Override
    public void removeAll() {

    }

    @Override
    public boolean has(Runnable task) {
        return false;
    }
}
