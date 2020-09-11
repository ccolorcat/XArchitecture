package x.common.util.stream;

import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public class LineReaderSupplier extends BaseSupplier<String> {
    private final LineNumberReader reader;
    private String line;

    public LineReaderSupplier(LineNumberReader reader) {
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
    public boolean hasNext() throws IOException {
        super.hasNext();
        return line != null;
    }

    @Override
    public String next() throws IOException {
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
