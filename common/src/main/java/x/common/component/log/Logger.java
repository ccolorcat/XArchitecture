package x.common.component.log;


import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.XLruCache;
import x.common.util.Utils;
import x.common.util.function.Producer;


/**
 * Author: cxx
 * Date: 2020-07-13
 * GitHub: https://github.com/ccolorcat
 */
public class Logger {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    public static final int ALL = 1;
    public static final int NONE = 8;

    private static final String CLASS_NAME = Logger.class.getName();
    private static final String SEPARATOR = "=";

    private static final String DEFAULT_TAG = "Client";
    private static final XLruCache<String, Logger> LOGGER = new XLruCache<>(6);
    private static final int MAX_LENGTH = 4000;
    private static int sThreshold;
    private static LogPrinter sPrinter;
    private static int sStackTraceDepth;
    private static int sSeparatorHalfLength;

    static {
        IClient client = Hummingbird.getClient();
        sThreshold = client.loggable() ? ALL : NONE;
        sPrinter = Hummingbird.visit(LogPrinter.class);
        sStackTraceDepth = 3;
        sSeparatorHalfLength = 50;
    }

    public static void setThreshold(int threshold) {
        sThreshold = threshold;
    }

    public static void setLogPrinter(@NonNull LogPrinter printer) {
        sPrinter = Utils.requireNonNull(printer, "printer == null");
    }

    public static void setTraceDepth(int depth) {
        sStackTraceDepth = Math.max(depth, 0);
    }

    public static void setSeparatorHalfLength(int halfLength) {
        sSeparatorHalfLength = Math.max(halfLength, 30);
    }

    public static Logger getLogger(@NonNull Object object) {
        return getLogger(Utils.emptyElse(object.getClass().getSimpleName(), DEFAULT_TAG));
    }

    public static Logger getLogger(@NonNull String tag) {
        if (Utils.isEmpty(tag)) throw new IllegalArgumentException("name is empty");
        Logger logger = LOGGER.get(tag);
        if (logger == null) {
            logger = new Logger(tag);
            LOGGER.put(tag, logger);
        }
        return logger;
    }

    public static Logger getDefault() {
        return getLogger(DEFAULT_TAG);
    }

    private final String tag;

    public void v(String msg) {
        print(VERBOSE, msg);
    }

    public void v(Producer<CharSequence> producer) {
        print(VERBOSE, producer);
    }

    public void d(String msg) {
        print(DEBUG, msg);
    }

    public void d(Producer<CharSequence> producer) {
        print(DEBUG, producer);
    }

    public void i(String msg) {
        print(INFO, msg);
    }

    public void i(Producer<CharSequence> producer) {
        print(INFO, producer);
    }

    public void w(String msg) {
        print(WARN, msg);
    }

    public void w(Producer<CharSequence> producer) {
        print(WARN, producer);
    }

    public void e(String msg) {
        print(ERROR, msg);
    }

    public void e(Producer<CharSequence> producer) {
        print(ERROR, producer);
    }

    public void e(Throwable throwable) {
        if (ERROR >= sThreshold) {
            throwable.printStackTrace();
        }
    }

    public void log(int priority, String msg) {
        print(priority, msg);
    }

    private void print(int priority, Producer<CharSequence> producer) {
        if (priority >= sThreshold && producer != null) {
            print(priority, producer.apply());
        }
    }

    private void print(int priority, CharSequence msg) {
        if (priority < sThreshold || Utils.isEmpty(msg)) return;
        StringBuilder builder = new StringBuilder();
        int lineLength;

        builder.append(" \n");
        concatRepeat(builder, SEPARATOR, sSeparatorHalfLength).append(' ').append(tag).append(' ');
        concatRepeat(builder, SEPARATOR, sSeparatorHalfLength);
        lineLength = builder.length() - 2;
        concatStackTrace(builder);

        builder.append("\n\n").append(msg).append('\n');
        concatRepeat(builder, SEPARATOR, lineLength / SEPARATOR.length());
        String log = builder.toString();
        final int length = log.length();
        if (length <= MAX_LENGTH) {
            sPrinter.println(priority, tag, log);
            return;
        }
        for (int start = 0, end; start < length; start = end) {
            end = friendlyEnd(log, start, Math.min(start + MAX_LENGTH, length));
            sPrinter.println(priority, tag, log.substring(start, end));
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

    private static StringBuilder concatStackTrace(StringBuilder builder) {
        if (sStackTraceDepth <= 0) return builder;
        boolean matched = false;
        int count = 0;
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            if (count >= sStackTraceDepth) break;
            String msg = e.toString();
            if (msg.startsWith(CLASS_NAME)) {
                matched = true;
            } else if (matched) {
                ++count;
                builder.append('\n').append(msg);
            }
        }
        return builder;
    }

    private static StringBuilder concatRepeat(StringBuilder builder, String str, int count) {
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder;
    }

    private Logger(String tag) {
        this.tag = tag;
    }
}
