package x.common.component.store;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;

import x.common.util.JsonUtils;


/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
class JsonStoreSerializer implements StoreSerializer {
    @Override
    public <T> String serialize(T value) {
        return JsonUtils.toJson(value);
    }

    @Override
    public <T> T deserialize(String value, @NonNull Type typeOfT) {
        return value == null ? null : JsonUtils.fromJson(value, typeOfT);
    }
}
