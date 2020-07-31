package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import retrofit2.Call;
import x.common.util.Once;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-17
 * GitHub: https://github.com/ccolorcat
 */
class MCallImpl<T> implements MCall<T> {
    static <T> MCallImpl<T> create(@NonNull Call<T> call) {
        return new MCallImpl<>(Utils.requireNonNull(call, "call == null"));
    }

    final Once<MCallback<T>> once = new Once<>();
    @NonNull
    final Call<T> delegate;

    MCallImpl(@NonNull Call<T> delegate) {
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public T execute() throws IOException {
        return delegate.execute().body();
    }

    @NonNull
    @Override
    public synchronized Discardable enqueue(@NonNull ApiCallback<? super T> callback) {
        once.set(MCallback.create(callback));
        try {
            once.get().callStart(checkCall());
            delegate.enqueue(once.get());
        } catch (Throwable t) {
            ApiException cause;
            if (t instanceof ApiException) {
                cause = (ApiException) t;
            } else {
                String msg = Utils.nullElse(t.getMessage(), HttpStatus.MSG_UNKNOWN);
                cause = new ApiException(HttpStatus.STATUS_UNKNOWN, msg, t);
            }
            once.get().callFailureAndFinish(cause);
        }
        return once.get();
    }

    @Override
    public boolean isExecuted() {
        return delegate.isExecuted();
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }

    @Override
    public boolean isCanceled() {
        return delegate.isCanceled();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NonNull
    @Override
    public MCall<T> clone() {
        return new MCallImpl<>(delegate.clone());
    }

    @Nullable
    private ApiException checkCall() {
        if (delegate.isCanceled()) return new ApiException(HttpStatus.STATUS_CANCELED, HttpStatus.MSG_CANCELED);
        if (delegate.isExecuted()) return new ApiException(HttpStatus.STATUS_EXECUTED, HttpStatus.MSG_EXECUTED);
        return null;
    }
}
