package x.common.component.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import x.common.component.Hummingbird;
import x.common.component.XLruCache;
import x.common.component.log.Logger;


/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
final class StoreHandler implements InvocationHandler {
    private final XLruCache<Method, StoreExecutor> cachedExecutors = new XLruCache<>(16);
    private final Store store;
    private final StoreSerializer serializer;
    private final boolean loggable;

    StoreHandler(@NonNull Store store, @NonNull StoreSerializer serializer, boolean loggable) {
        this.store = store;
        this.serializer = serializer;
        this.loggable = loggable;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StoreExecutor executor = cachedExecutors.get(method);
        if (executor == null) {
            executor = parse(method, args);
            cachedExecutors.put(method, executor);
        } else if (loggable) {
            Logger.getLogger(Hummingbird.TAG).v("hit cached StoreExecutor: " + method + '=' + executor);
        }
        return executor.execute(args);
    }

    private StoreExecutor parse(Method method, Object[] args) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation a : annotations) {
            if (a instanceof Read) return newReadExecutor(method, annotations, (Read) a);
            if (a instanceof Remove) return newRemoveExecutor(method, annotations, (Remove) a, args);
            if (a instanceof Clear) return newClearExecutor(method, annotations, args);
        }
        return newWriteExecutor(method, args);


//        Read read = Reflects.search(annotations, Read.class);
//        if (read != null) return newReadExecutor(method, annotations, read);
//        Remove remove = Reflects.search(annotations, Remove.class);
//        if (remove != null) return newRemoveExecutor(method, annotations, remove, args);
//        Clear clear = Reflects.search(annotations, Clear.class);
//        if (clear != null) return newClearExecutor(method, annotations, args);
//        return newWriteExecutor(method, args);
    }

    private StoreExecutor newReadExecutor(Method method, Annotation[] annotations, Read read) {
        assertNotExists(method, annotations, Remove.class, Clear.class);
        Type returnType = method.getGenericReturnType();
        if (returnType == Void.class || returnType == Void.TYPE) {
            throw new IllegalArgumentException(method + " returns void");
        }
        Type[] parameterTypes = method.getGenericParameterTypes();
        if (parameterTypes.length > 1) {
            throw new IllegalArgumentException(method + " require at most one parameter");
        }
        if (parameterTypes.length == 1 && returnType != parameterTypes[0]) {
            throw new IllegalArgumentException(method + " parameter type and return type must be same");
        }
        return new ReadStoreExecutor(store, serializer, read.value(), returnType);
    }

    private StoreExecutor newRemoveExecutor(Method method, Annotation[] annotations, Remove remove, Object[] args) {
        assertNotExists(method, annotations, Read.class, Clear.class);
        assertNoArgs(method, args);
        Type returnType = method.getGenericReturnType();
        if (returnType != Void.TYPE && returnType != Boolean.TYPE) {
            throw new IllegalArgumentException(method + " must return void or boolean");
        }
        return new RemoveStoreExecutor(store, remove.value(), returnType == Boolean.TYPE);
    }

    private StoreExecutor newClearExecutor(Method method, Annotation[] annotations, Object[] args) {
        assertNotExists(method, annotations, Read.class, Remove.class);
        assertNoArgs(method, args);
        Type returnType = method.getGenericReturnType();
        if (returnType != Void.TYPE && returnType != Boolean.TYPE) {
            throw new IllegalArgumentException(method + " must return void or boolean");
        }
        return new ClearStoreExecutor(store, returnType == Boolean.TYPE);
    }

    private StoreExecutor newWriteExecutor(Method method, Object[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException(method + " require at least one parameter");
        }
        Type returnType = method.getGenericReturnType();
        if (returnType != Void.TYPE && returnType != Boolean.TYPE) {
            throw new IllegalArgumentException(method + " must return void or boolean");
        }
        String[] keys = getWriteParameterAnnotations(method);
        if (keys.length != args.length) {
            throw new IllegalArgumentException(method + " every parameter must have @Write");
        }
        return new WriteStoreExecutor(store, serializer, keys, returnType == Boolean.TYPE);
    }

    @SafeVarargs
    private static void assertNotExists(Method method, Annotation[] all, Class<? extends Annotation>... maybe) {
        for (Class<? extends Annotation> type : maybe) {
            if (search(all, type) != null) {
                throw new IllegalArgumentException(method + " has move than one annotation(@Clear, @Remove, @Read)");
            }
        }
    }

    private static void assertNoArgs(Method method, Object[] args) {
        if (args != null && args.length != 0) {
            throw new IllegalArgumentException(method + " cannot have parameters");
        }
    }

    private static String[] getWriteParameterAnnotations(Method method) {
        Annotation[][] pas = method.getParameterAnnotations();
        String[] result = new String[pas.length];
        for (int i = 0, size = pas.length; i < size; ++i) {
            Write write = search(pas[i], Write.class);
            if (write == null) throw new IllegalArgumentException(method + " missing @Write");
            result[i] = write.value();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends Annotation> T search(Annotation[] annotations, Class<T> tClass) {
        for (Annotation annotation : annotations) {
            if (tClass.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }
}
