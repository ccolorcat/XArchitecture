package x.common.test;


import x.common.component.schedule.MainXScheduler;
import x.common.component.schedule.SchedulerWrapper;

/**
 * Author: cxx
 * Date: 2020-07-28
 * GitHub: https://github.com/ccolorcat
 */
final class TestMainScheduler extends SchedulerWrapper implements MainXScheduler {
    protected TestMainScheduler() {
        super(1);
    }
}
