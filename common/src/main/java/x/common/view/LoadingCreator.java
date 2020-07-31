package x.common.view;

import android.content.Context;

import androidx.annotation.NonNull;

import x.common.component.annotation.Stateless;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
@Stateless(LoadingCreatorImpl.class)
public interface LoadingCreator {
    @NonNull
    Showable create(@NonNull Context context);
}
