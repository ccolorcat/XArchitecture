package x.common.component.network;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
public interface ApiCallback<T> {
    default void onStart() {}

    default void onSuccess(@NonNull T data) {}

    default void onFailure(@NonNull ApiException cause) {}

    default void onFinish() {}
}
