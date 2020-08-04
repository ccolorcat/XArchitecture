package x.common.util;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
public final class Utils {
    public static String checkedUrl(@NonNull String url) {
        if (!url.toLowerCase().matches("^(http)(s)?://(\\S)+")) {
            throw new IllegalArgumentException("Bad url = " + url + ", the scheme must be http or https");
        }
        return url;
    }

    public static long quiteToLong(String number, long defaultValue) {
        if (isEmpty(number)) return defaultValue;
        try {
            return Long.parseLong(number);
        } catch (Throwable e) {
            Logger.getDefault().e(e);
        }
        return defaultValue;
    }

    public static <T> T nullElse(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T extends CharSequence> T emptyElse(T text, T defaultValue) {
        return isEmpty(text) ? defaultValue : text;
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence text) {
        return !isEmpty(text);
    }

    @NonNull
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) throw new NullPointerException(message);
        return obj;
    }

    @NonNull
    public static <T> T requireNonNull(T obj) {
        if (obj == null) throw new NullPointerException();
        return obj;
    }

    public static void sleep(@NonNull TimeUnit unit, long duration) {
        sleep(unit.toMillis(duration));
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable ignore) {
        }
    }

    public static String md5(@NonNull String input) {
        String result = requireNonNull(input, "input == null");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(input.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder(bytes.length << 1);
            for (byte b : bytes) {
                sb.append(Character.forDigit((b & 0xf0) >> 4, 16))
                        .append(Character.forDigit(b & 0x0f, 16));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException ignore) {
        }
        return result;
    }

    private Utils() {
        throw new AssertionError("no instance");
    }
}
