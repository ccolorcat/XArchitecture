package x.common.component;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import x.common.IClient;
import x.common.component.annotation.AnnotationProcessor;
import x.common.component.annotation.ApiModel;
import x.common.component.annotation.ApiModelProcessor;
import x.common.component.annotation.Core;
import x.common.component.annotation.CoreProcessor;
import x.common.component.annotation.Stateful;
import x.common.component.annotation.StatefulProcess;
import x.common.component.annotation.Stateless;
import x.common.component.annotation.StatelessProcessor;
import x.common.component.annotation.StoreModel;
import x.common.component.annotation.StoreModelProcessor;
import x.common.component.log.Logger;
import x.common.util.Once;
import x.common.util.Reflects;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue"})
public final class Hummingbird {
    public static final String TAG = "Hummingbird";

    private static final Map<Class<?>, Class<?>> sCachedStateful = new HashMap<>();
    private static final Map<Class<?>, Object> sCachedStateless = new HashMap<>();
    private static final WeakHashMap<Class<?>, Object> sCached = new WeakHashMap<>();
    private static final Once<IClient> sClient = new Once<>();
    private static final Map<Class<? extends Annotation>, AnnotationProcessor> sProcessors = new HashMap<>();


    private static void setupAnnotationProcessor() {
        sProcessors.put(Core.class, new CoreProcessor<>());
        sProcessors.put(ApiModel.class, new ApiModelProcessor<>());
        sProcessors.put(StoreModel.class, new StoreModelProcessor<>());
        sProcessors.put(Stateful.class, new StatefulProcess<>());
        sProcessors.put(Stateless.class, new StatelessProcessor<>());
    }

    public static void init(@NonNull IClient client) {
        sClient.set(client);
        setupAnnotationProcessor();
    }

    @NonNull
    public static IClient getClient() {
        return sClient.get();
    }

    @NonNull
    public static <T> T visit(@NonNull Class<T> tClass) {
        T result = byRegisteredStateful(tClass);
        if (result != null) return result;
        result = byRegisteredStateless(tClass);
        if (result != null) return result;

        if (tClass.getAnnotation(Stateful.class) != null) return byAnnotationProcessor(tClass);
        result = (T) sCached.get(tClass);
        if (result == null) {
            result = byAnnotationProcessor(tClass);
            sCached.put(tClass, Utils.requireNonNull(result, "AnnotationProcessor returned null"));
        } else if (sClient.get().loggable()) {
            Logger.getLogger(TAG).v("hit cached: " + tClass.getName() + '=' + result.getClass().getName());
        }
        return result;
    }

    public static <T> boolean registerStateless(@NonNull Class<? super T> tClass, @NonNull T impl) {
        sCachedStateless.put(Utils.requireNonNull(tClass), Utils.requireNonNull(impl));
        return true;
    }

    public static <T> boolean unregisterStateless(@NonNull Class<? super T> tClass) {
        sCachedStateless.remove(tClass);
        return true;
    }

    public static <T> boolean registerStateful(@NonNull Class<? super T> tClass, @NonNull Class<? extends T> impl) {
        sCachedStateful.put(Utils.requireNonNull(tClass), Utils.requireNonNull(impl));
        return true;
    }

    public static <T> boolean unregisterStateful(@NonNull Class<? super T> tClass) {
        sCachedStateful.remove(tClass);
        return true;
    }

    @Nullable
    private static <T> T byRegisteredStateful(@NonNull Class<T> tClass) {
        Class<?> impl = sCachedStateful.get(tClass);
        if (impl != null) {
            try {
                return (T) Reflects.newDefaultInstance(impl);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Nullable
    private static <T> T byRegisteredStateless(@NonNull Class<T> tClass) {
        return (T) sCachedStateless.get(tClass);
    }

    @NonNull
    private static <T> T byAnnotationProcessor(Class<T> tClass) {
        AnnotationProcessor processor;
        for (Annotation a : tClass.getAnnotations()) {
            processor = sProcessors.get(a.annotationType());
            if (processor != null) {
                try {
                    return (T) processor.process(tClass, a, sClient.get());
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        }
        throw new IllegalArgumentException("No AnnotationProcessor can process " + tClass);
    }

    private Hummingbird() {
        throw new AssertionError("no instance");
    }
}
