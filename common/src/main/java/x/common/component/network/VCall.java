package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

/**
 * Author: cxx
 * Date: 2020-07-20
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("UnusedReturnValue")
public interface VCall<T> extends MCall<T> {
    @NonNull
    Discardable enqueue(@NonNull LifecycleOwner owner, @NonNull ApiCallback<? super T> callback);

    @NonNull
    @Override
    VCall<T> clone();
}
