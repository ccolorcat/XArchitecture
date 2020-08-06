package x.common.component.loader;

import android.widget.ImageView;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-08-05
 * GitHub: https://github.com/ccolorcat
 */
public interface ImageHandler {
    boolean handle(@NonNull ImageLoader loader, @NonNull ImageView view);
}
