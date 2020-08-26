package x.common.util;

import androidx.annotation.NonNull;

import org.json.JSONObject;

/**
 * Author: cxx
 * Date: 2020-08-25
 * GitHub: https://github.com/ccolorcat
 */
public class JsonStoreReader extends BaseStoreReader<JSONObject> {

    public JsonStoreReader(@NonNull JSONObject store) {
        super(store);
    }

    @Override
    public boolean contains(@NonNull String key) {
        return store != null && store.has(key);
    }

    @Override
    public String getString(@NonNull String key, String defaultValue) {
        return store != null ? store.optString(key, defaultValue) : defaultValue;
    }
}
