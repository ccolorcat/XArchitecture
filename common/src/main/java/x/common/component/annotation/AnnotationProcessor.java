package x.common.component.annotation;

import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;

import x.common.IClient;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public interface AnnotationProcessor<T, A extends Annotation> {
    @NonNull
    T process(@NonNull Class<T> tClass, @NonNull A annotation, @NonNull IClient client) throws Throwable;
}
