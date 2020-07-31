package x.common.component.log;

import android.util.Log;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class AndroidLogPrinter implements LogPrinter {
    private AndroidLogPrinter() { }

    @Override
    public void println(int priority, String tag, String msg) {
        Log.println(priority, tag, msg);
    }
}
