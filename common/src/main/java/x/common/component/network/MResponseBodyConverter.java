package x.common.component.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
final class MResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    @NonNull
    private final Gson gson;
    @NonNull
    private final TypeAdapter<T> adapter;

    MResponseBodyConverter(@NonNull Gson gson, @NonNull TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
}
