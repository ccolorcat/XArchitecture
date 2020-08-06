package cc.colorcat.login;

import android.content.Context;

import androidx.annotation.NonNull;

import cc.colorcat.login.view.LoginFragment;
import x.common.FragmentLauncher;
import x.common.component.Hummingbird;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
final class LoginNavigationImpl implements LoginNavigation {
    @Override
    public void launch(@NonNull Context context) {
        Hummingbird.visit(FragmentLauncher.class).launch(context, LoginFragment.class);
    }
}
