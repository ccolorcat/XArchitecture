package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
public interface MCall<T> extends Cloneable {
    @Nullable
    T execute() throws IOException;

    @NonNull
    Discardable enqueue(@NonNull ApiCallback<? super T> callback);

    boolean isExecuted();

    void cancel();

    boolean isCanceled();

    @NonNull
    MCall<T> clone();
}
