package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import x.common.util.IoUtils;

/**
 * Author: cxx
 * Date: 2020-04-16
 */
public class StringReader implements DataReader<String> {
    @Override
    public String read(@NonNull InputStream input) throws IOException {
        return IoUtils.justRead(IoUtils.bufferedReader(input));
    }
}
