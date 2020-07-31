package x.common.component.schedule;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
final class BackgroundHandlerXSchedulerImpl extends HandlerXScheduler implements BackgroundHandlerXScheduler {
    BackgroundHandlerXSchedulerImpl() {
        HandlerThread ht = new HandlerThread("background-scheduler");
        ht.setDaemon(true);
        ht.start();
        setHandler(new Handler(ht.getLooper()));
    }
}
