package x.common;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import x.common.component.annotation.Stateless;
import x.common.view.BaseFragment;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
@Stateless(FragmentLauncherImpl.class)
public interface FragmentLauncher {
    default void launch(@NonNull Context context, @NonNull Class<? extends BaseFragment> clazz) {
        this.launch(context, clazz, null);
    }

    void launch(@NonNull Context context, @NonNull Class<? extends BaseFragment> clazz, Bundle args);
}
