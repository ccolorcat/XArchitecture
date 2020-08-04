package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: cxx
 * Date: 2020-04-13
 */
public interface DataReader<T> {
    T read(@NonNull InputStream input) throws IOException;
}
