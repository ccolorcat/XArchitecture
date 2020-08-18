package x.common.component.loader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import x.common.component.finder.FileOperator;
import x.common.component.finder.Filename;
import x.common.component.log.Logger;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-06
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("ALL")
public final class ImageLoader {
    private static final String GIF = "gif";
    private static final String GIF_WITH_DOT = '.' + GIF;

    private static final Logger LOGGER = Logger.getLogger("ImageLoader");
    private static final Map<Object, Uri> CACHED = new WeakHashMap<>();
    private static final List<ImageHandler> HANDLERS = new ArrayList<>();

    static {
        HANDLERS.add(new CachedImageHandler());
        HANDLERS.add(new HttpImageHandler());
        HANDLERS.add(new DefaultImageHandler());
    }


    private boolean asGif = false;
    private boolean asGifByGuess = false;

    @NonNull
    final Uri uri;
    @NonNull
    final FileOperator operator;
    @NonNull
    final Filename filename;
    @NonNull
    RequestBuilder<? extends Drawable> builder;

    ImageLoader(
            @NonNull RequestManager manager,
            @NonNull Uri uri,
            @NonNull FileOperator operator,
            @NonNull Filename filename
    ) {
        this.uri = uri;
        this.operator = operator;
        this.filename = filename;
        this.builder = manager.asDrawable();
        this.builder.diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    public ImageLoader asGif() {
        asGif = true;
        builder = builder.decode(GifDrawable.class);
        return this;
    }

    public ImageLoader asGifByGuess() {
        asGifByGuess = true;
        if (isGifByGuess()) {
            builder = builder.decode(GifDrawable.class);
        }
        return this;
    }

    public ImageLoader asBitmap() {
        builder = builder.decode(Bitmap.class);
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
        builder.transform(Utils.requireNonNull(transformer, "transformer == null"));
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
        Utils.requireNonNull(view, "view == null");
        LOGGER.v("fetch image from Uri: " + uri);
        CACHED.put(view, uri);
        for (ImageHandler handler : HANDLERS) {
            if (handler.handle(this, view)) {
                break;
            }
        }
    }

    void attach(ImageView view, Uri uri) {
        LOGGER.d("image attached uri = " + uri);
        Uri attachedUri = CACHED.get(view);
        if (this.uri.equals(attachedUri)) {
            CACHED.remove(view);
//            if (isGif()) builder = builder.decode(GifDrawable.class);
            builder.load(uri).into(view);
        }
    }

    private boolean isGif() {
        if (asGif) return true;
        if (asGifByGuess) {
            if (GIF.equalsIgnoreCase(filename.type)) return true;
            String path = uri.getPath();
            return path != null && path.toLowerCase().endsWith(GIF_WITH_DOT);
        }
        return false;
    }

    private boolean isGifByGuess() {
        if (GIF.equalsIgnoreCase(filename.type)) return true;
        String path = uri.getPath();
        return path != null && path.toLowerCase().endsWith(GIF_WITH_DOT);
    }
}
