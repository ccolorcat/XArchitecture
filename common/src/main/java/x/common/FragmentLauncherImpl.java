package x.common;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import x.common.view.BaseFragment;
import x.common.view.ContainerActivity;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
class FragmentLauncherImpl implements FragmentLauncher {
    @Override
    public void launch(@NonNull Context context, @NonNull Class<? extends BaseFragment> clazz, Bundle args) {
        ContainerActivity.launch(context, clazz, args);
    }
}
