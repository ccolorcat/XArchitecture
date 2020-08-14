package x.common.component.loader;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.finder.DownloadWriter;
import x.common.component.finder.FileOperator;
import x.common.component.log.Logger;
import x.common.component.schedule.BackgroundXScheduler;
import x.common.component.schedule.MainXScheduler;
import x.common.component.schedule.XScheduler;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-06
 * GitHub: https://github.com/ccolorcat
 */
public final class Downloader {
    private final Lazy<BackgroundXScheduler> io = Lazy.by(() -> Hummingbird.visit(BackgroundXScheduler.class));
    private final Uri uri;
    private final FileOperator operator;
    private XScheduler dispatcher = Hummingbird.visit(MainXScheduler.class);

    Downloader(@NonNull Uri uri, @NonNull FileOperator operator) {
        this.uri = uri;
        this.operator = operator;
    }

    public Downloader on(@NonNull XScheduler scheduler) {
        this.dispatcher = Utils.requireNonNull(scheduler);
        return this;
    }

    @Nullable
    public FileOperator getIfExists() {
        return operator.exists() ? operator : null;
    }

    @Nullable
    public FileOperator execute() {
        return execute(null);
    }

    @Nullable
    public FileOperator execute(DownloadListener listener) {
        return createTask(listener).execute();
    }

    public void fetch() {
        fetch(null);
    }

    public void fetch(DownloadListener listener) {
        io.get().execute(createTask(listener));
    }

    private DownloadTask createTask(DownloadListener listener) {
        return new DownloadTask(uri, operator, dispatcher, listener);
    }


    private static class DownloadTask implements Runnable {
        private final Uri uri;
        private final FileOperator operator;
        private final XScheduler dispatcher;
        private final DownloadListener listener;

        private DownloadTask(Uri uri, FileOperator operator, XScheduler dispatcher, DownloadListener listener) {
            this.uri = uri;
            this.operator = operator;
            this.dispatcher = dispatcher;
            this.listener = listener;
        }

        @Override
        public void run() {
            execute();
        }

        @Nullable
        private FileOperator execute() {
            if (listener != null) dispatcher.execute(this::dispatchStart);
            Throwable throwable = null;
            try {
                operator.write(uri.toString(), createWriter());
            } catch (Throwable t) {
                throwable = t;
            } finally {
                if (listener != null) {
                    final Throwable cause = throwable;
                    dispatcher.execute(() -> dispatchResult(cause));
                }
            }
            return throwable == null ? operator : null;
        }

        private DownloadWriter createWriter() {
            return listener == null ? DownloadWriter.create() : DownloadWriter.of(dispatcher, listener);
        }

        private void dispatchStart() {
            try {
                listener.onStart();
            } catch (Throwable t) {
                Logger.getDefault().e(t);
            }
        }

        private void dispatchResult(Throwable cause) {
            try {
                if (cause == null) {
                    listener.onSuccess(operator);
                } else {
                    listener.onFailure(cause);
                }
            } finally {
                listener.onFinish();
            }
        }
    }
}

