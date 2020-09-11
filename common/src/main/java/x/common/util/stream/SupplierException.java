package x.common.util.stream;

/**
 * Author: cxx
 * Date: 2020-05-25
 * GitHub: https://github.com/ccolorcat
 */
public class SupplierException extends RuntimeException {
    SupplierException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "SupplierException{} " + super.toString();
    }
}
