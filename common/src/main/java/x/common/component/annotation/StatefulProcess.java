package x.common.component.annotation;

import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
public final class StatefulProcess<T> implements AnnotationProcessor<T, Stateful> {
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public T process(@NonNull Class<T> tClass, @NonNull Stateful annotation, @NonNull IClient client) throws Throwable {
        Class<?> impl = annotation.value();
//        if (impl != Void.class && Checker.assertImpl(tClass, impl)) return (T) impl.newInstance();
        if (impl == Void.class || !Checker.assertImpl(tClass, impl)) {
            String className = annotation.className();
            impl = Utils.isEmpty(className) ? tClass : Class.forName(className);
        }
        return (T) impl.newInstance();
    }
}
