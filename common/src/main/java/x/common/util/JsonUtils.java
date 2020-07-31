package x.common.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public final class JsonUtils {
    private static final Gson GSON = new GsonBuilder().create();

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T fromJson(@NonNull String json, @NonNull Class<T> classOfT) {
        Utils.requireNonNull(json, "json == null");
        Utils.requireNonNull(classOfT, "classOfT == null");
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(@NonNull String json, @NonNull Type typeOfT) {
        Utils.requireNonNull(json, "json == null");
        Utils.requireNonNull(typeOfT, "typeOfT == null");
        return GSON.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(@NonNull Reader reader, @NonNull Class<T> classOfT) {
        Utils.requireNonNull(reader, "reader == null");
        Utils.requireNonNull(classOfT, "classOfT == null");
        return GSON.fromJson(reader, classOfT);
    }

    public static <T> T fromJson(@NonNull Reader reader, @NonNull Type typeOfT) {
        Utils.requireNonNull(reader, "json == null");
        Utils.requireNonNull(typeOfT, "typeOfT == null");
        return GSON.fromJson(reader, typeOfT);
    }

    public static Map<String, String> fromJson(@NonNull String json) {
        return GSON.fromJson(json, new TypeToken<Map<String, String>>() {}.getType());
    }

    private JsonUtils() {
        throw new AssertionError("no instance");
    }
}
