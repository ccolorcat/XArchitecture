package x.common.component.network;

/**
 * Author: cxx
 * Date: 2020-07-21
 * GitHub: https://github.com/ccolorcat
 */
public final class HttpStatus {
    public static final int STATUS_UNKNOWN = -70;
    public static final String MSG_UNKNOWN = "unknown";

    public static final int STATUS_EXECUTED = -80;
    public static final String MSG_EXECUTED = "already executed";

    public static final int STATUS_CANCELED = -90;
    public static final String MSG_CANCELED = "canceled";

    public static final int STATUS_CONNECTION_ERROR = -100;
    public static final String MSG_CONNECTION_ERROR = "connection error";

    private HttpStatus() {
        throw new AssertionError("no instance");
    }
}
