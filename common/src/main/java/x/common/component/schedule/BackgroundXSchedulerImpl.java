package x.common.component.schedule;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
final class BackgroundXSchedulerImpl extends SchedulerWrapper implements BackgroundXScheduler {
    BackgroundXSchedulerImpl() {
        super(Runtime.getRuntime().availableProcessors() << 1, new XThreadFactory("background-scheduler"));
    }
}
