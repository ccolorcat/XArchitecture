package x.common.component.network;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-06-30
 * GitHub: https://github.com/ccolorcat
 */
public interface ApiFactory {
    @NonNull
    <T> T create(Class<T> tClass);
}
