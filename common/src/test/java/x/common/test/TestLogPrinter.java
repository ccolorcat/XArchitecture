package x.common.test;

import x.common.component.log.LogPrinter;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class TestLogPrinter implements LogPrinter {
    @Override
    public void println(int priority, String tag, String msg) {
        System.out.println(tag + ": " + msg);
    }
}
