package x.common.util;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
public final class IoUtils {
    private static final int BUFFER_SIZE = 4096;

    @NonNull
    public static BufferedInputStream newBufferedInputStream(@NonNull File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    @NonNull
    public static BufferedOutputStream newBufferedOutputStream(@NonNull File file) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    @NonNull
    public static String justRead(@NonNull Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        for (int length = reader.read(buffer); length != -1; length = reader.read(buffer)) {
            builder.append(buffer, 0, length);
        }
        return builder.toString();
    }

    public static void justDump(@NonNull InputStream input, @NonNull OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        for (int length = input.read(buffer); length != -1; length = input.read(buffer)) {
            output.write(buffer, 0, length);
        }
        output.flush();
    }

    @NonNull
    public static BufferedInputStream buffered(@NonNull InputStream input) {
        return input instanceof BufferedInputStream ? (BufferedInputStream) input : new BufferedInputStream(input);
    }

    @NonNull
    public static BufferedOutputStream buffered(@NonNull OutputStream output) {
        return output instanceof BufferedOutputStream ? (BufferedOutputStream) output : new BufferedOutputStream(output);
    }

    @NonNull
    public static BufferedReader bufferedReader(@NonNull InputStream input) {
        return new BufferedReader(new InputStreamReader(input));
    }

    @NonNull
    public static BufferedWriter bufferedWriter(@NonNull OutputStream output) {
        return new BufferedWriter(new OutputStreamWriter(output));
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }

    private IoUtils() {
        throw new AssertionError("no instance");
    }
}
