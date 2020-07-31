package x.common.component.runtime;

import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public interface ResultHandler {
    void onResult(@RuntimeForResult.Result int resultCode, @Nullable Intent data);
}
