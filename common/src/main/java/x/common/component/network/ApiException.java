package x.common.component.network;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
public final class ApiException extends RuntimeException {
    private static final long serialVersionUID = -823434544234523523L;

    private final int status;

    ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    ApiException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @NonNull
    @Override
    public String toString() {
        return "ApiException: status = " + status + ", message = " + getLocalizedMessage();
    }
}
