package cc.colorcat.login;

import android.content.Context;

import androidx.annotation.NonNull;

import x.common.component.annotation.Stateless;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
@Stateless(className = "cc.colorcat.login.LoginNavigationImpl")
public interface LoginNavigation {
    void launch(@NonNull Context context);
}
