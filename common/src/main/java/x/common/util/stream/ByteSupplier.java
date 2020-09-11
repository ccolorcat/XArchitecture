package x.common.util.stream;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-05-21
 * GitHub: https://github.com/ccolorcat
 */
public final class ByteSupplier extends BaseSupplier<byte[]> {
    private final InputStream bis;
    private final byte[] buffer = new byte[8192];
    private byte[] bytes;

    public ByteSupplier(@NonNull InputStream input) {
        bis = buffered(Utils.requireNonNull(input, "input == null"));
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
    public boolean hasNext() throws Throwable {
        super.hasNext();
        return bytes != null;
    }

    @Override
    public byte[] next() throws Throwable {
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
