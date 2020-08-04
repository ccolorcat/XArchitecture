package x.common.component.finder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import x.common.component.log.Logger;
import x.common.util.IoUtils;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
final class FileOperatorImpl implements FileOperator {
    private final DiskCache.Snapshot snapshot;

    FileOperatorImpl(@NonNull DiskCache.Snapshot snapshot) {
        this.snapshot = Utils.requireNonNull(snapshot, "snapshot == null");
    }

    @NonNull
    @Override
    public URI getUri() {
        return snapshot.getUri();
    }

    @Override
    public <T> boolean quietWrite(T src, DataWriter<? super T> writer) {
        if (src == null || writer == null) return false;
        try {
            write(src, writer);
            return true;
        } catch (IOException e) {
            Logger.getDefault().e(e);
            return false;
        }
    }

    @Override
    public <T> void write(@NonNull T src, @NonNull DataWriter<? super T> writer) throws IOException {
        try (OutputStream output = IoUtils.buffered(snapshot.newOutputStream())) {
            writer.write(src, output);
            output.flush();
        } catch (IOException e) {
            delete();
            throw e;
        }
    }

    @Nullable
    @Override
    public <T> T quietRead(DataReader<? extends T> reader) {
        if (reader == null) return null;
        try {
            return read(reader);
        } catch (IOException e) {
            Logger.getDefault().e(e);
            return null;
        }
    }

    @Override
    public <T> T read(@NonNull DataReader<? extends T> reader) throws IOException {
        try (InputStream input = IoUtils.buffered(snapshot.newInputStream())) {
            return reader.read(input);
        }
    }

    @Override
    public boolean exists() {
        return size() > 0L;
    }

    @Override
    public long size() {
        return snapshot.getContentLength();
    }

    @Override
    public void delete() {
        try {
            snapshot.requireDelete();
        } catch (IOException e) {
            Logger.getDefault().e(e);
        }
    }
}
