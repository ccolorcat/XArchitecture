package x.common.component.store;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
public interface StoreFactory {
    @NonNull
    <T> T create(@NonNull Class<T> tClass);
}
