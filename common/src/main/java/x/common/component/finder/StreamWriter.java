package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import x.common.util.IoUtils;

/**
 * Author: cxx
 * Date: 2020-04-14
 */
public class StreamWriter implements DataWriter<InputStream> {
    @Override
    public void write(@NonNull InputStream src, @NonNull OutputStream output) throws IOException {
        IoUtils.justDump(src, output);
    }
}
