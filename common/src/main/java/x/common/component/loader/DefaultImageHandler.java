package x.common.component.loader;

import android.widget.ImageView;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-08-06
 * GitHub: https://github.com/ccolorcat
 */
final class DefaultImageHandler implements ImageHandler {
    @Override
    public boolean handle(@NonNull ImageLoader loader, @NonNull ImageView view) {
        loader.attach(view, loader.uri);
        return true;
    }
}
