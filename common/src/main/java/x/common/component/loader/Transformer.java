package x.common.component.loader;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Author: cxx
 * Date: 2020-04-17
 */
public abstract class Transformer extends BitmapTransformation {
    private final String id = getKey();
    private final byte[] id_bytes = id.getBytes(CHARSET);

    @Override
    protected final Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return transform(toTransform);
    }

    protected abstract Bitmap transform(@NonNull Bitmap bitmap);

    @Override
    public final boolean equals(Object o) {
        return getClass().isAssignableFrom(o.getClass());
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    @Override
    public final void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(id_bytes);
    }

    private String getKey() {
        return getClass().getCanonicalName();
    }
}
