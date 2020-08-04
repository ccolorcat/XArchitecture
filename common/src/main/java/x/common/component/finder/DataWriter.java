package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: cxx
 * Date: 2020-04-13
 */
public interface DataWriter<T> {
    /**
     * @throws IOException if write failed.
     */
    void write(@NonNull T src, @NonNull OutputStream output) throws IOException;
}
