package x.common.util.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

/**
 * Author: cxx
 * Date: 2020-05-22
 * GitHub: https://github.com/ccolorcat
 */
public class CharReaderSupplier extends BaseSupplier<char[]> {
    private final Reader reader;
    private final char[] buffer = new char[4096];
    private char[] chars;

    public CharReaderSupplier(Reader reader) {
        this.reader = buffer(reader);
        try {
            preRead();
        } catch (IOException ignore) {
            chars = null;
        }
    }

    private void preRead() throws IOException {
        int length = reader.read(buffer);
        if (length != -1) {
            chars = Arrays.copyOf(buffer, length);
        } else {
            chars = null;
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        super.hasNext();
        return chars != null;
    }

    @Override
    public char[] next() throws IOException {
        super.next();
        char[] next = chars;
        preRead();
        return next;
    }

    @Override
    public void close() throws IOException {
        reader.close();
        super.close();
    }

    private static Reader buffer(Reader reader) {
        return reader instanceof BufferedReader ? reader : new BufferedReader(reader);
    }
}
