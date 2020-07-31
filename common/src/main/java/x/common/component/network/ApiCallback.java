package x.common.component.network;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
public interface ApiCallback<T> {
    void onStart();

    void onSuccess(@NonNull T data);

    void onFailure(@NonNull ApiException cause);

    void onFinish();
}
