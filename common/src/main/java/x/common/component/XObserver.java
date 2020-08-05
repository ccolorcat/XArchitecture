package x.common.component;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-08-05
 * GitHub: https://github.com/ccolorcat
 */
public interface XObserver<T> {
    void onChanged(@NonNull T value);
}
