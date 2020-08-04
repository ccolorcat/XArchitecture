package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import x.common.util.IoUtils;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
public class FileReader implements DataReader<File> {
    public final File savePath;

    public FileReader(@NonNull File savePath) {
        this.savePath = Utils.requireNonNull(savePath);
    }

    @Override
    public File read(@NonNull InputStream input) throws IOException {
        try (OutputStream output = IoUtils.newBufferedOutputStream(savePath)) {
            IoUtils.justDump(input, output);
            return savePath;
        }
    }
}
