package x.common.component.store;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;

/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
public interface StoreSerializer {
    <T> String serialize(T value);

    <T> T deserialize(String value, @NonNull Type typeOfT);
}
