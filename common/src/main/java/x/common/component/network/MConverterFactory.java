package x.common.component.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
class MConverterFactory extends Converter.Factory {
    public static MConverterFactory create() {
        return create(new Gson());
    }

    public static MConverterFactory create(@NonNull Gson gson) {
        return new MConverterFactory(Utils.requireNonNull(gson, "gson == null"));
    }

    @NonNull
    private final Gson gson;

    private MConverterFactory(@NonNull Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return (Converter<ResponseBody, String>) ResponseBody::string;
        }
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit
    ) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MRequestBodyConverter<>(gson, adapter);
    }
}
