package x.common.component.loader;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestBuilder;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.finder.DownloadWriter;
import x.common.component.finder.FileOperator;
import x.common.component.log.Logger;
import x.common.component.monitor.NetworkMonitor;
import x.common.component.schedule.IoXScheduler;
import x.common.component.schedule.MainXScheduler;
import x.common.util.AndroidUtils;

/**
 * Author: cxx
 * Date: 2020-08-05
 * GitHub: https://github.com/ccolorcat
 */
final class HttpImageHandler implements ImageHandler {
    private final Logger logger = ObjectLoader.LOGGER;
    private final Lazy<IoXScheduler> io = Lazy.by(() -> Hummingbird.visit(IoXScheduler.class));
    private final Lazy<MainXScheduler> main = Lazy.by(() -> Hummingbird.visit(MainXScheduler.class));
    private final Lazy<NetworkMonitor> monitor = Lazy.by(() -> Hummingbird.visit(NetworkMonitor.class));


    @Override
    public boolean handle(@NonNull ImageLoader loader, @NonNull ImageView view) {
        String scheme = loader.uri.getScheme();
        if (scheme == null || !scheme.toLowerCase().startsWith("http")) return false;
        if (monitor.get().isAvailable()) {
            logger.v("download image: " + loader.uri);
            setPlaceHolder(loader.builder, view);
            io.get().execute(new ImageTask(loader, view, main.get()));
        } else {
            loader.attach(view, loader.uri);
        }
        return true;
    }

    private void setPlaceHolder(RequestBuilder<? extends Drawable> builder, ImageView view) {
        int id = builder.getPlaceholderId();
        if (id > 0) {
            view.setImageResource(id);
            return;
        }
        Drawable holder = builder.getPlaceholderDrawable();
        if (holder != null) view.setImageDrawable(holder);
    }

    private static class ImageTask implements Runnable {
        private final ImageLoader loader;
        private final Reference<ImageView> ref;
        private final MainXScheduler main;

        private ImageTask(ImageLoader loader, ImageView view, MainXScheduler main) {
            this.loader = loader;
            this.ref = new WeakReference<>(view);
            this.main = main;
        }

        @Override
        public void run() {
            FileOperator fo = loader.operator;
            if (fo.quietWrite(loader.uri.toString(), DownloadWriter.create()) && fo.exists()) {
                ImageView view = ref.get();
                if (view == null) return;
                Uri uri = AndroidUtils.toUri(fo.getUri());
                main.execute(() -> loader.attach(view, uri));
            }
        }
    }
}
