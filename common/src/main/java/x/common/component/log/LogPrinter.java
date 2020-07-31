package x.common.component.log;


import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
@Core(AndroidLogPrinter.class)
public interface LogPrinter {
    void println(int priority, String tag, String msg);
}
