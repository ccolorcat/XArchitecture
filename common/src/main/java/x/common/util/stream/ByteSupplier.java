package x.common.util.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public class ByteSupplier extends BaseSupplier<byte[]> {
    private final InputStream bis;
    private final byte[] buffer = new byte[8192];
    private byte[] bytes;

    public ByteSupplier(InputStream input) {
        bis = buffered(input);
        try {
            preRead();
        } catch (IOException ignore) {
            bytes = null;
        }
    }

    private void preRead() throws IOException {
        int length = bis.read(buffer);
        if (length != -1) {
            bytes = Arrays.copyOf(buffer, length);
        } else {
            bytes = null;
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        super.hasNext();
        return bytes != null;
    }

    @Override
    public byte[] next() throws IOException {
        super.next();
        byte[] next = bytes;
        preRead();
        return next;
    }

    @Override
    public void close() throws IOException {
        bis.close();
        super.close();
    }

    private static InputStream buffered(InputStream input) {
        return input instanceof BufferedInputStream ? input : new BufferedInputStream(input);
    }
}
