package x.common.component.loader;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import x.common.component.finder.FileOperator;
import x.common.component.log.Logger;
import x.common.util.AndroidUtils;

/**
 * Author: cxx
 * Date: 2020-08-06
 * GitHub: https://github.com/ccolorcat
 */
final class CachedImageHandler implements ImageHandler {
    private final Logger logger = ObjectLoader.LOGGER;

    @Override
    public boolean handle(@NonNull ImageLoader loader, @NonNull ImageView view) {
        FileOperator operator = loader.operator;
        if (operator.exists()) {
            Uri uri = AndroidUtils.toUri(operator.getUri());
            logger.v("hit cached image\nremote: " + loader.uri + "\ncached: " + uri);
            loader.attach(view, uri);
            return true;
        }
        return false;
    }
}
