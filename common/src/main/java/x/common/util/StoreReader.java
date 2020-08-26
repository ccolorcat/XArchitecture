package x.common.util;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-08-25
 * GitHub: https://github.com/ccolorcat
 */
public interface StoreReader {
    @NonNull
    static StoreReader of(Map<String, String> store) {
        return store != null ? new MapStoreReader(store) : BaseStoreReader.empty();
    }

    @NonNull
    static StoreReader of(JSONObject store) {
        return store != null ? new JsonStoreReader(store) : BaseStoreReader.empty();
    }

    @NonNull
    static StoreReader fromJson(String json) {
        if (Utils.isEmpty(json)) return BaseStoreReader.empty();
        try {
            JSONObject store = new JSONObject(json);
            return new JsonStoreReader(store);
        } catch (JSONException e) {
            return BaseStoreReader.empty();
        }
    }

    boolean contains(@NonNull String key);

    default int getInt(@NonNull String key) {
        return getInt(key, 0);
    }

    default int getInt(@NonNull String key, int defaultValue) {
        try {
            String s = getString(key, null);
            return Utils.isNotEmpty(s) ? Integer.parseInt(s) : defaultValue;
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
        return defaultValue;
    }

    default boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    default boolean getBoolean(@NonNull String key, boolean defaultValue) {
        try {
            String s = getString(key, null);
            return Utils.isNotEmpty(s) ? Boolean.parseBoolean(s) : defaultValue;
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
        return defaultValue;
    }

    default long getLong(@NonNull String key) {
        return getLong(key, 0L);
    }

    default long getLong(@NonNull String key, long defaultValue) {
        try {
            String s = getString(key, null);
            return Utils.isNotEmpty(s) ? Long.parseLong(s) : defaultValue;
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
        return defaultValue;
    }

    default double getDouble(@NonNull String key) {
        return getDouble(key, 0.0);
    }

    default double getDouble(@NonNull String key, double defaultValue) {
        try {
            String s = getString(key, null);
            return Utils.isNotEmpty(s) ? Double.parseDouble(s) : defaultValue;
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
        return defaultValue;
    }

    default String getString(@NonNull String key) {
        return getString(key, "");
    }

    String getString(@NonNull String key, String defaultValue);
}
