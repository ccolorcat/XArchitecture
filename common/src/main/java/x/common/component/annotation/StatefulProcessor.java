package x.common.component.annotation;

import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.util.Reflects;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public final class StatefulProcessor<T> implements AnnotationProcessor<T, Stateful> {
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull Stateful annotation, @NonNull IClient client) throws Throwable {
        Class<?> impl = Parsers.requireImpl(tClass, annotation.value(), annotation.className());
        return (T) Reflects.newDefaultInstance(impl);
    }
}
