package x.common.component.log;


import androidx.annotation.NonNull;

import java.util.Locale;

import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.XLruCache;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-13
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("unused")
public class Logger {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    public static final int ALL = 1;
    public static final int NONE = 8;

    private static final String DEFAULT_TAG = "Client";
    private static final XLruCache<String, Logger> LOGGER = new XLruCache<>(6);
    private static final int MAX_LENGTH = 1024 * 2;
    private static int sThreshold;
    private static LogPrinter sPrinter;

    static {
        IClient client = Hummingbird.getClient();
        sThreshold = client.isTest() ? ALL : NONE;
        sPrinter = Hummingbird.visit(LogPrinter.class);
    }

    public static void setGlobalThreshold(int threshold) {
        sThreshold = threshold;
    }

    public static void setGlobalLogPrinter(@NonNull LogPrinter printer) {
        sPrinter = Utils.requireNonNull(printer, "printer == null");
    }

    public static Logger getLogger(@NonNull Object object) {
        return getLogger(Utils.emptyElse(object.getClass().getSimpleName(), DEFAULT_TAG));
    }

    public static Logger getLogger(String name) {
        if (Utils.isEmpty(name)) throw new IllegalArgumentException("name is empty");
        Logger logger = LOGGER.get(name);
        if (logger == null) {
            logger = new Logger(name);
            LOGGER.put(name, logger);
        }
        return logger;
    }

    public static Logger getDefault() {
        return getLogger(DEFAULT_TAG);
    }

    private final String tag;


    public void v(String format, Object... args) {
        log(tag, VERBOSE, format, args);
    }

    public void d(String format, Object... args) {
        log(tag, DEBUG, format, args);
    }

    public void i(String format, Object... args) {
        log(tag, INFO, format, args);
    }

    public void w(String format, Object... args) {
        log(tag, WARN, format, args);
    }

    public void e(String format, Object... args) {
        log(tag, ERROR, format, args);
    }

    public void e(Throwable throwable) {
        if (ERROR >= sThreshold) {
            throwable.printStackTrace();
        }
    }

    public void log(int priority, String format, Object... args) {
        log(tag, priority, format, args);
    }

    private static void log(String tag, int priority, String format, Object... args) {
        if (priority < sThreshold) return;
        tag = String.valueOf(tag);
        String msg = format(format, args);
        final int length = msg.length();
        if (length <= MAX_LENGTH) {
            sPrinter.println(priority, tag, msg);
            return;
        }
        for (int start = 0, end; start < length; start = end) {
            end = friendlyEnd(msg, start, Math.min(start + MAX_LENGTH, length));
            sPrinter.println(priority, tag, msg.substring(start, end));
        }
    }

    private static int friendlyEnd(String msg, int start, int end) {
        if (msg.length() == end || msg.charAt(end) == '\n') {
            return end;
        }
        for (int last = end - 1; last > start; --last) {
            if (msg.charAt(last) == '\n') {
                return last + 1;
            }
        }
        return end;
    }

    private static String format(String format, Object... args) {
        if (args == null || args.length == 0) {
            return format;
        }
        return String.format(Locale.getDefault(), format, args);
    }

    private Logger(String tag) {
        this.tag = tag;
    }
}
