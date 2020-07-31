package x.common.component.runtime;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public interface PermissionsHandler {
    void onAllGranted(@NonNull String[] permissions);

    void onDeniedOccur(@NonNull String[] granted, @NonNull String[] denied);
}
