package x.common.util.stream;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public final class LineReaderSupplier extends BaseSupplier<String> {
    private final LineNumberReader reader;
    private String line;

    public LineReaderSupplier(@NonNull LineNumberReader reader) {
        this.reader = reader;
        try {
            preRead();
        } catch (IOException ignore) {
            line = null;
        }
    }

    private void preRead() throws IOException {
        line = reader.readLine();
    }

    @Override
    public boolean hasNext() throws Throwable {
        super.hasNext();
        return line != null;
    }

    @Override
    public String next() throws Throwable {
        super.next();
        String next = line;
        preRead();
        return next;
    }

    @Override
    public void close() throws IOException {
        reader.close();
        super.close();
    }
}
