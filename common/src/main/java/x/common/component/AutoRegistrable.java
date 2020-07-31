package x.common.component;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
public interface AutoRegistrable<T> extends Registrable<T> {
    void bind(@NonNull LifecycleOwner owner, @NonNull T t);
}
