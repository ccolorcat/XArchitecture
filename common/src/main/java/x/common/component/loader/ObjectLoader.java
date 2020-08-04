package x.common.component.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import x.common.component.Hummingbird;
import x.common.component.finder.FileOperator;
import x.common.component.finder.Filename;
import x.common.component.finder.FinderCore;
import x.common.component.finder.Module;
import x.common.component.log.Logger;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-17
 */
public class ObjectLoader {
    private static final Logger LOGGER = Logger.getLogger("ObjectLoader");

    private static final String SCHEME_RESOURCE = "ol";
    private static final String PATH_DRAWABLE = "/drawable";
    private static final String PARAM_ID = "id";

    public static ObjectLoader with(@NonNull Context context) {
        return new ObjectLoader(Glide.with(context));
    }

    public static ObjectLoader with(@NonNull View view) {
        return new ObjectLoader(Glide.with(view));
    }

    public static ObjectLoader with(@NonNull Fragment fragment) {
        return new ObjectLoader(Glide.with(fragment));
    }

    public static Uri fromDrawableResource(int id) {
        String uri = SCHEME_RESOURCE + "://object.loader" + PATH_DRAWABLE + "?" + PARAM_ID + '=' + id;
        return Uri.parse(uri);
    }

    private static final Map<Object, Uri> TARGETS = new WeakHashMap<>();
    private static final FinderCore FINDER = Hummingbird.visit(FinderCore.class);

    private RequestManager manager;
    private Uri uri;

    private String moduleName = Module.UNKNOWN, moduleId = Module.UNKNOWN;
    private Filename filename;

    private ObjectLoader(RequestManager manager) {
        this.manager = manager;
    }

    public ObjectLoader asChatModule(String chatId) {
        return module(Module.CHAT, chatId);
    }

    public ObjectLoader asAlbumModule(String albumId) {
        return module(Module.ALBUM, albumId);
    }

    public ObjectLoader asQuickService(String id) {
        return module(Module.QUICK_SERVICE, id);
    }

    public ObjectLoader module(@Module String moduleName, String moduleId) {
        this.moduleName = Utils.requireNonNull(moduleName, "moduleName == null");
        this.moduleId = Utils.requireNonNull(moduleId, "moduleId == null");
        return this;
    }

    public ObjectLoader filename(@NonNull Filename filename) {
        this.filename = Utils.requireNonNull(filename, "filename == null");
        return this;
    }

    public ObjectLoader filename(String name, String type) {
        this.filename = Filename.of(name, type);
        return this;
    }

    public ObjectLoader filename(String filename) {
        this.filename = Filename.from(filename);
        return this;
    }

    public ObjectLoader load(@NonNull File file) {
        this.uri = Uri.fromFile(file);
        return this;
    }

    public ObjectLoader load(@NonNull String url) {
        this.uri = Uri.parse(url);
        return this;
    }

    public ObjectLoader load(@NonNull Uri uri) {
        this.uri = uri;
        return this;
    }

//    public void fetch(@NonNull FetchListener<File> listener) {
//        Utils.requireNonNull(listener, "listener == null");
//        FileOperator operator = checkAndResolve();
//        if (operator == null) {
//            Utils.loadWithGlide(manager, uri, null, listener);
//            return;
//        }
//        if (operator.exists()) {
//            listener.onSuccess(new File(operator.getUri().getPath()));
//            return;
//        }
//        String scheme = Utils.nullElse(uri.getScheme(), "").toLowerCase();
//        if (scheme.startsWith("http") && NetworkMonitor.get().isAvailable()) {
//            Utils.loadHttpWithFileOperator(operator, uri, listener);
//            return;
//        }
//        if (scheme.startsWith("file")) {
//            Utils.loadFileAndSave(operator, uri, listener);
//            return;
//        }
//        Utils.loadWithGlide(manager, uri, operator, listener);
//    }

    public ImageLoader asImage() {
        return newImageLoader();
    }

    private ImageLoader newImageLoader() {
        FileOperator operator = checkAndResolve();
        return new ImageLoader(manager, uri, operator, filename);
    }

    @Nullable
    public FileOperator resolve() {
        return checkAndResolve();
    }

    @Nullable
    private FileOperator checkAndResolve() {
        Utils.requireNonNull(uri, "uri == null");
        if (filename == null) {
            filename = Filename.fromUri(uri.toString());
        }
        return FINDER.getFileOperator(moduleName, moduleId, filename);
    }


    @SuppressLint("CheckResult")
    public static class ImageLoader {
        private static final String GIF = "gif";
        private static final String GIF_WITH_DOT = '.' + GIF;

        private boolean asGif = false;
        private boolean asGifByGuess = false;


        private final Uri originalUri;
        private final FileOperator operator;
        private final Filename filename;
        private RequestBuilder<? extends Drawable> builder;

        private ImageLoader(RequestManager manager, Uri uri, FileOperator operator, Filename filename) {
            this.originalUri = uri;
            this.operator = operator;
            this.filename = filename;
            this.builder = manager.asDrawable();
            this.builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }

        public ImageLoader asGif() {
            asGif = true;
            return this;
        }

        public ImageLoader asGifByGuess() {
            asGifByGuess = true;
            return this;
        }

        public ImageLoader placeholder(@DrawableRes int resourceId) {
            builder.placeholder(resourceId);
            return this;
        }

        public ImageLoader placeholder(Drawable drawable) {
            builder.placeholder(drawable);
            return this;
        }

        public ImageLoader error(@DrawableRes int resourceId) {
            builder.error(resourceId);
            return this;
        }

        public ImageLoader error(Drawable drawable) {
            builder.error(drawable);
            return this;
        }

        public ImageLoader centerCrop() {
            builder.centerCrop();
            return this;
        }

        public ImageLoader centerInside() {
            builder.centerInside();
            return this;
        }

        public ImageLoader circleCrop() {
            builder.circleCrop();
            return this;
        }

        public ImageLoader fitCenter() {
            builder.fitCenter();
            return this;
        }

        public ImageLoader transform(@NonNull Transformer transformer) {
            builder.transform(transformer);
            return this;
        }

        public ImageLoader override(int width, int height) {
            builder.override(width, height);
            return this;
        }

        public ImageLoader override(int size) {
            builder.override(size);
            return this;
        }

        public void into(@NonNull ImageView view) {
//            Utils.requireNonNull(view, "view == null");
//            TARGETS.put(view, originalUri);
//            LOGGER.v("remote uri = " + originalUri + ", cached uri: " + operator);
//            String scheme = originalUri.getScheme();
//            if (operator == null || scheme == null) {
//                attach(view, originalUri);
//                return;
//            }
//            if (operator.exists()) {
//                attach(view, operator.getUri());
//                return;
//            }
//            if (scheme.toLowerCase().startsWith("http")) {
//                if (NetworkMonitor.get().isAvailable()) {
//                    setPlaceHolder(view);
//                    operator.writeAsync(originalUri.toString(), DownloadWriter.get(), new DownloadListener(view));
//                } else {
//                    attach(view, originalUri);
//                }
//                return;
//            }
//            attach(view, originalUri);
        }

        private void attach(ImageView view, Uri uri) {
            LOGGER.d("attach uri = " + uri);
            Uri attachedUri = TARGETS.remove(view);
            if (!originalUri.equals(attachedUri)) {
                LOGGER.w(originalUri + " be replaced " + attachedUri);
                return;
            }
            if (isGif()) {
                builder = builder.decode(GifDrawable.class);
            }
            if (SCHEME_RESOURCE.equalsIgnoreCase(uri.getScheme())
                    && PATH_DRAWABLE.equalsIgnoreCase(uri.getPath())) {
                String resId = uri.getQueryParameter(PARAM_ID);
                if (resId != null) {
                    int id = Integer.parseInt(resId);
                    builder.load(id).into(view);
                    return;
                }
            }
            builder.load(uri).into(view);
        }

        private boolean isGif() {
            if (asGif) return true;
            if (asGifByGuess) {
                return originalUri.toString().toLowerCase().endsWith(GIF_WITH_DOT) || GIF.equalsIgnoreCase(filename.type);
            }
            return false;
        }

        private void setPlaceHolder(ImageView view) {
            Drawable placeholder = builder.getPlaceholderDrawable();
            if (placeholder != null) {
                view.setImageDrawable(placeholder);
            } else {
                int id = builder.getPlaceholderId();
                if (id > 0) {
                    view.setImageResource(id);
                }
            }
        }

//        private class DownloadListener implements Consumer<Boolean> {
//            private Reference<ImageView> viewRef;
//
//            private DownloadListener(ImageView view) {
//                viewRef = new WeakReference<>(view);
//            }
//
//            @Override
//            public void accept(Boolean success) {
//                LOGGER.v("download " + success + " " + originalUri);
//                ImageView view = viewRef.get();
//                if (view != null) {
//                    attach(view, success ? operator.getUri() : originalUri);
//                } else {
//                    LOGGER.w(originalUri + " attach view recycled.");
//                }
//            }
//        }
    }

    public interface FetchListener<T> {
        default void onSuccess(@NonNull T t) {}

        default void onFailure() {}
    }
}
