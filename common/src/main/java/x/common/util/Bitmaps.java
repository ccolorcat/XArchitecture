package x.common.util;

import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Author: cxx
 * Date: 2020-08-19
 * GitHub: https://github.com/ccolorcat
 */
public final class Bitmaps {
    public static Size parseSize(@NonNull File path) {
        if (Utils.requireNonNull(path).exists()) {
            return parseSize(path.getAbsolutePath());
        }
        return new Size(0, 0);
    }

    public static Size parseSize(@NonNull String path) {
        Utils.requireNonNull(path, "path == null");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return new Size(options.outWidth, options.outHeight);
    }


    private Bitmaps() {
        throw new AssertionError("no instance");
    }
}
