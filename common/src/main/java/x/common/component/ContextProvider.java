package x.common.component;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.common.component.annotation.Core;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-26
 * GitHub: https://github.com/ccolorcat
 */
@Core(ContextProviderImpl.class)
public interface ContextProvider {
    @NonNull
    default Context requireContext() {
        Context context = getActivityContext();
        if (context == null) context = getApplicationContext();
        return Utils.requireNonNull(context, "getApplicationContext returned null");
    }

    @NonNull
    Context getApplicationContext();

    @Nullable
    Context getActivityContext();
}
