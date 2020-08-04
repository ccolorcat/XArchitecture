package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import x.common.util.IoUtils;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
public class FileWriter implements DataWriter<File> {
    @Override
    public void write(@NonNull File src, @NonNull OutputStream output) throws IOException {
        try (InputStream input = IoUtils.newBufferedInputStream(src)) {
            IoUtils.justDump(input, output);
        }
    }
}
