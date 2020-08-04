package x.common.component.schedule;

import android.os.Handler;
import android.os.Looper;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
class AndroidMainXScheduler extends HandlerXSchedulerImpl implements MainXScheduler {
    AndroidMainXScheduler() {
        super(new Handler(Looper.getMainLooper()));
    }
}
