package x.common.component.store;

import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import x.common.component.XLruCache;
import x.common.util.Reflects;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-09-08
 * GitHub: https://github.com/ccolorcat
 */
final class StoreFactoryImpl implements StoreFactory {
    private final XLruCache<Method, StoreExecutor> cached = new XLruCache<>(16);
    private final Store store;
    private final StoreSerializer serializer;

    StoreFactoryImpl(@NonNull Store store, @NonNull StoreSerializer serializer) {
        this.store = Utils.requireNonNull(store, "store == null");
        this.serializer = Utils.requireNonNull(serializer, "serializer == null");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> T create(@NonNull Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, (proxy, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(tClass, args);
            }
            return cached.unsafeGetOrPut(method, () -> parse(method, args)).execute(args);
        });
    }

    @NonNull
    private StoreExecutor parse(Method method, Object[] args) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation a : annotations) {
            if (a instanceof Read) return newReadExecutor(method, annotations, (Read) a);
            if (a instanceof Remove) return newRemoveExecutor(method, annotations, (Remove) a, args);
            if (a instanceof Clear) return newClearExecutor(method, annotations, args);
        }
        return newWriteExecutor(method, args);
    }

    @NonNull
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

    @NonNull
    private StoreExecutor newRemoveExecutor(Method method, Annotation[] annotations, Remove remove, Object[] args) {
        assertNotExists(method, annotations, Read.class, Clear.class);
        assertNoArgs(method, args);
        Type returnType = method.getGenericReturnType();
        if (returnType != Void.TYPE && returnType != Boolean.TYPE) {
            throw new IllegalArgumentException(method + " must return void or boolean");
        }
        return new RemoveStoreExecutor(store, remove.value(), returnType == Boolean.TYPE);
    }

    @NonNull
    private StoreExecutor newClearExecutor(Method method, Annotation[] annotations, Object[] args) {
        assertNotExists(method, annotations, Read.class, Remove.class);
        assertNoArgs(method, args);
        Type returnType = method.getGenericReturnType();
        if (returnType != Void.TYPE && returnType != Boolean.TYPE) {
            throw new IllegalArgumentException(method + " must return void or boolean");
        }
        return new ClearStoreExecutor(store, returnType == Boolean.TYPE);
    }

    @NonNull
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
            if (Reflects.search(all, type) != null) {
                throw new IllegalArgumentException(method + " has move than one annotation(@Clear, @Remove, @Read)");
            }
        }
    }

    private static void assertNoArgs(Method method, Object[] args) {
        if (args != null && args.length != 0) {
            throw new IllegalArgumentException(method + " cannot have parameters");
        }
    }

    @NonNull
    private static String[] getWriteParameterAnnotations(Method method) {
        Annotation[][] pas = method.getParameterAnnotations();
        String[] result = new String[pas.length];
        for (int i = 0, size = pas.length; i < size; ++i) {
            Write write = Reflects.search(pas[i], Write.class);
            if (write == null) throw new IllegalArgumentException(method + " missing @Write");
            result[i] = write.value();
        }
        return result;
    }
}
