package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import x.common.component.log.Logger;
import x.common.component.schedule.XScheduler;
import x.common.util.IoUtils;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
public class DownloadWriter implements DataWriter<String> {
    protected static final int TIME_OUT_CONNECT = 30 * 1000;
    protected static final int TIME_OUT_READ = 30 * 1000;

    private static final String CONTENT_LENGTH = "Content-Length";


    public static DownloadWriter create() {
        return new DownloadWriter(null);
    }

    public static DownloadWriter of(@NonNull ProgressListener listener) {
        return new DownloadWriter(Utils.requireNonNull(listener, "listener == null"));
    }

    public static DownloadWriter of(@NonNull XScheduler scheduler, @NonNull ProgressListener listener) {
        Utils.requireNonNull(scheduler, "scheduler == null");
        Utils.requireNonNull(listener, "listener == null");
        ProgressListener spl = new ScheduledProgressListener(scheduler, listener);
        return new DownloadWriter(spl);
    }

    private final ProgressListener listener;
    private HttpURLConnection conn;

    private DownloadWriter(ProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public void write(@NonNull String src, @NonNull OutputStream output) throws IOException {
        URL url = new URL(Utils.checkedUrl(src));
        HttpURLConnection conn = null;
        InputStream input = null;
        try {
            conn = createConnection(url);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                this.conn = conn;
                input = conn.getInputStream();
                input = IoUtils.buffered(wrap(conn, input));
                IoUtils.justDump(input, output);
            }
        } finally {
            disconnect(conn);
            IoUtils.close(input);
            this.conn = null;
        }
    }

//    public void cancel() {
//        disconnect(conn);
//    }

    private static void disconnect(HttpURLConnection conn) {
        if (conn != null) {
            try {
                conn.disconnect();
            } catch (Throwable throwable) {
                Logger.getDefault().e(throwable);
            }
        }
    }

    private InputStream wrap(HttpURLConnection conn, InputStream input) {
        if (listener != null && input != null) {
            long contentLength = Utils.quiteToLong(conn.getHeaderField(CONTENT_LENGTH), -1L);
            if (contentLength > 0L) {
                return ProgressInputStream.of(input, contentLength, listener);
            }
        }
        return input;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(TIME_OUT_CONNECT);
        conn.setReadTimeout(TIME_OUT_READ);
        conn.setRequestMethod("GET");
        return conn;
    }

    private static class ScheduledProgressListener implements ProgressListener {
        private final XScheduler scheduler;
        private final ProgressListener listener;

        private ScheduledProgressListener(XScheduler scheduler, ProgressListener listener) {
            this.scheduler = scheduler;
            this.listener = listener;
        }

        @Override
        public void onProgress(long finished, long total, int percent) {
            scheduler.execute(() -> listener.onProgress(finished, total, percent));
        }
    }
}
