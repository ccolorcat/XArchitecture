package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.ConnectException;
import java.util.Objects;

import javax.net.SocketFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import x.common.component.Hummingbird;
import x.common.component.log.Logger;
import x.common.component.schedule.MainXScheduler;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-16
 * GitHub: https://github.com/ccolorcat
 */
final class MCallback<T> implements Callback<T>, Discardable {
    static <T> MCallback<T> create(@NonNull ApiCallback<? super T> callback) {
        return new MCallback<>(Utils.requireNonNull(callback, "callback == null"));
    }


    private final Object lock = new Object();
    @NonNull
    private ApiCallback<? super T> callback;

    private MCallback(@NonNull ApiCallback<? super T> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        int status = HttpStatus.STATUS_UNKNOWN;
        String msg = HttpStatus.MSG_UNKNOWN;
        T data = null;
        ApiException exception = null;
        try {
            status = response.code();
            msg = response.message();
            data = response.body();
        } catch (Throwable t) {
            exception = new ApiException(status, Utils.nullElse(t.getMessage(), msg), t);
        } finally {
            if (status == 200 && data != null) {
                callSuccessAndFinish(data);
            } else {
                if (exception == null) exception = new ApiException(status, msg);
                callFailureAndFinish(exception);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        int status = HttpStatus.STATUS_UNKNOWN;
        String msg = HttpStatus.MSG_UNKNOWN;
        if (t instanceof HttpException) {
            status = ((HttpException) t).code();
        } else if (isConnectError(t)) {
            status = HttpStatus.STATUS_CONNECTION_ERROR;
            msg = HttpStatus.MSG_CONNECTION_ERROR;
        }
        msg = Utils.emptyElse(t.getMessage(), msg);
        callFailureAndFinish(new ApiException(status, msg, t));
    }

    @Override
    public void discard() {
        synchronized (lock) {callback = MCallback.EMPTY_CALLBACK;}
    }

    void callStart(@Nullable final ApiException ex) {
        call(this::realCallStart);
        if (ex != null) throw ex;
    }

    private void realCallStart() {
        try {
            synchronized (lock) { callback.onStart(); }
        } catch (Throwable e) {
            Logger.getDefault().e(e);
        }
    }

    void callFailureAndFinish(@NonNull ApiException ex) {
        call(() -> realCallFailureAndFinish(ex));
    }

    private void realCallFailureAndFinish(@NonNull ApiException ex) {
        synchronized (lock) {
            try {
                callback.onFailure(ex);
            } finally {
                callback.onFinish();
            }
        }
    }

    void callSuccessAndFinish(@NonNull T data) {
        call(() -> realCallSuccessAndFinish(data));
    }

    private void realCallSuccessAndFinish(@NonNull T data) {
        synchronized (lock) {
            try {
                callback.onSuccess(data);
            } finally {
                callback.onFinish();
            }
        }
    }


    private static final ApiCallback<Object> EMPTY_CALLBACK = new ApiCallback<Object>() {
        @Override
        public void onStart() { }

        @Override
        public void onSuccess(@NonNull Object data) { }

        @Override
        public void onFailure(@NonNull ApiException cause) { }

        @Override
        public void onFinish() { }
    };

    private static final MainXScheduler MAIN = Hummingbird.visit(MainXScheduler.class);

    private static final String JAVA_NET;
    private static final String JAVAX_NET;

    static {
        String javaNet = "java.net";
        try {
            javaNet = Objects.requireNonNull(ConnectException.class.getPackage()).getName();
        } catch (Throwable ignore) {
        } finally {
            JAVA_NET = javaNet;
        }

        String javaxNet = "javax.net";
        try {
            javaxNet = Objects.requireNonNull(SocketFactory.class.getPackage()).getName();
        } catch (Throwable ignore) {
        } finally {
            JAVAX_NET = javaxNet;
        }
    }

    private static void call(@NonNull Runnable runnable) {
        MAIN.execute(runnable);
    }

    private static boolean isConnectError(@NonNull Throwable t) {
        String name = t.getClass().getName();
        return name.startsWith(JAVA_NET) || name.startsWith(JAVAX_NET);
    }
}
