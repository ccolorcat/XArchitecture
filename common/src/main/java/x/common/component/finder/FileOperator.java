package x.common.component.finder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.URI;

/**
 * Author: cxx
 * Date: 2020-04-15
 */
public interface FileOperator extends DataOperator {
    @NonNull
    URI getUri();

    <T> boolean quietWrite(T src, DataWriter<? super T> writer);

    <T> void write(@NonNull T src, @NonNull DataWriter<? super T> writer) throws IOException;

    @Nullable
    <T> T quietRead(DataReader<? extends T> reader);

    <T> T read(@NonNull DataReader<? extends T> reader) throws IOException;
}
