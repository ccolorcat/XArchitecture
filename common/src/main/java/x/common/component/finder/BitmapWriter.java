package x.common.component.finder;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-16
 */
public class BitmapWriter implements DataWriter<Bitmap> {

    public static BitmapWriter getDefault() {
        return Holder.DEFAULT;
    }

    /**
     * @param format  如果为空则按照 {@link Bitmap.CompressFormat#JPEG} 来保存。
     * @param quality [0, 100] 越大图片越保真，小于 0 会被置为 0，大于 100 会被置为 100.
     */
    public static BitmapWriter create(Bitmap.CompressFormat format, int quality) {
        Bitmap.CompressFormat f = Utils.nullElse(format, Bitmap.CompressFormat.JPEG);
        int q = quality < 0 ? 0 : Math.min(quality, 100);
        return new BitmapWriter(f, q);
    }

    private final Bitmap.CompressFormat format;
    private final int quality;

    private BitmapWriter(Bitmap.CompressFormat format, int quality) {
        this.format = format;
        this.quality = quality;
    }

    @Override
    public void write(@NonNull Bitmap src, @NonNull OutputStream output) throws IOException {
        if (!src.compress(format, quality, output)) {
            throw new IOException("compress bitmap(" + src + ") to OutputStream(" + output + ") failed.");
        }
    }

    private static class Holder {
        private static final BitmapWriter DEFAULT = new BitmapWriter(Bitmap.CompressFormat.JPEG, 100);
    }
}
