package x.common.util;

import androidx.annotation.NonNull;

import java.util.Map;

/**
 * Author: cxx
 * Date: 2020-08-25
 * GitHub: https://github.com/ccolorcat
 */
public class MapStoreReader extends BaseStoreReader<Map<String, String>> {
    public MapStoreReader(@NonNull Map<String, String> store) {
        super(store);
    }

    @Override
    public boolean contains(@NonNull String key) {
        return store != null && store.containsKey(key);
    }

    @Override
    public String getString(@NonNull String key, String defaultValue) {
        if (store == null) return defaultValue;
        String value = store.get(key);
        return value != null ? value : defaultValue;
    }
}
