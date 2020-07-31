package x.common.component.schedule;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
class IoXSchedulerImpl extends SchedulerWrapper implements IoXScheduler {
    IoXSchedulerImpl() {
        super(Runtime.getRuntime().availableProcessors() << 1, new XThreadFactory("io-scheduler"));
    }
}
