package x.common.component.network;

import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import x.common.component.Hummingbird;
import x.common.util.Reflects;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-25
 * GitHub: https://github.com/ccolorcat
 */
final class SignApiFactory implements ApiFactory {
    private final CommonInterceptor interceptor = Hummingbird.visit(CommonInterceptor.class);
    private final Set<Method> marked = new CopyOnWriteArraySet<>();
    private final Retrofit retrofit;
    private final String baseUrlTrunk;

    SignApiFactory(@NonNull Retrofit retrofit) {
        this.retrofit = Utils.requireNonNull(retrofit, "retrofit == null");
        this.baseUrlTrunk = Urls.getUrlTrunk(retrofit.baseUrl().toString());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> T create(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, new InvocationHandler() {
            private final T impl = retrofit.create(tClass);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (marked.contains(method)) return method.invoke(impl, args);

                final Sign sign = method.getAnnotation(Sign.class);
                final SignType type;
                if (sign == null || (type = sign.value()) == SignType.NONE) {
                    return method.invoke(impl, args);
                }

                String mark = parseMark(method, args);
                if (mark != null) {
                    interceptor.mark(mark, type);
                    marked.add(method);
                }
                return method.invoke(impl, args);
            }
        });
    }

    private String parseMark(Method method, Object[] args) {
        String path = parsePath(method);
        if (Utils.isNotEmpty(path)) {
            // 将 path 中的 "{xx}" (其中 xx 可以是 "/" 以外的任意字符) 替换为 "((?!/).)+" 以使 path 部分可以正则匹配。
            return URI.create(baseUrlTrunk)
                    .resolve(path.replaceAll("\\{((?!/).)+}", "((?!/).)+"))
                    .toString();
        }
        Annotation[][] pas = method.getParameterAnnotations();
        if (pas.length > 0 && Reflects.search(pas[0], Url.class) != null) {
            return Urls.getUrlTrunk(args[0].toString());
        }
        return null;
    }

    private static String parsePath(Method method) {
        for (Annotation a : method.getDeclaredAnnotations()) {
            if (a instanceof GET) return ((GET) a).value();
            if (a instanceof POST) return ((POST) a).value();
            if (a instanceof PUT) return ((PUT) a).value();
            if (a instanceof DELETE) return ((DELETE) a).value();
            if (a instanceof HEAD) return ((HEAD) a).value();
            if (a instanceof PATCH) return ((PATCH) a).value();
            if (a instanceof OPTIONS) return ((OPTIONS) a).value();
            if (a instanceof HTTP) return ((HTTP) a).path();
        }
        return null;
    }
}
