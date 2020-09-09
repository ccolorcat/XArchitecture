package x.common.component;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-09-07
 * GitHub: https://github.com/ccolorcat
 */
public final class HummingbirdException extends Exception {
    public HummingbirdException() {
    }

    public HummingbirdException(String message) {
        super(message);
    }

    public HummingbirdException(String message, Throwable cause) {
        super(message, cause);
    }

    public HummingbirdException(Throwable cause) {
        super(cause);
    }

    @NonNull
    @Override
    public String toString() {
        return "HummingbirdException{} " + super.toString();
    }
}
