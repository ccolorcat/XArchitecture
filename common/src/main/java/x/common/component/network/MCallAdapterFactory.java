package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
public final class MCallAdapterFactory extends CallAdapter.Factory {
    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NonNull final Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        final Class<?> rawType = getRawType(returnType);
        if (rawType != MCall.class && rawType != VCall.class) return null;
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("MCall return type must be parameterized as MCall<Foo> or MCall<? extends Foo>");
        }
        final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new CallAdapter<Object, MCall<?>>() {
            @NonNull
            @Override
            public Type responseType() {
                return responseType;
            }

            @NonNull
            @Override
            public MCall<?> adapt(@NonNull Call<Object> call) {
                return rawType == MCall.class ? MCallImpl.create(call) : VCallImpl.create(call);
            }
        };
    }
}
