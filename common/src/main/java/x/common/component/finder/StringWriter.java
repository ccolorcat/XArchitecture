package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import x.common.util.IoUtils;

/**
 * Author: cxx
 * Date: 2020-04-14
 */
public class StringWriter implements DataWriter<String> {
    @Override
    public void write(@NonNull String src, @NonNull OutputStream output) throws IOException {
        Writer writer = IoUtils.bufferedWriter(output);
        writer.write(src);
        writer.flush();
    }
}
