package x.common.component.loader;

import androidx.annotation.NonNull;

import x.common.component.finder.FileOperator;
import x.common.component.finder.ProgressListener;

/**
 * Author: cxx
 * Date: 2020-08-06
 * GitHub: https://github.com/ccolorcat
 */
public interface DownloadListener extends ProgressListener {
    default void onStart() {}

    @Override
    default void onProgress(long finished, long total, int percent) {}

    default void onSuccess(@NonNull FileOperator operator) {}

    default void onFailure(@NonNull Throwable cause) {}

    default void onFinish() {}
}
