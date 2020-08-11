package x.common.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cc.colorcat.adapter.ViewHolder;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public class XHolder extends ViewHolder<XHolder> {
    @NonNull
    public static XHolder from(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @LayoutRes int layoutId) {
        return new XHolder(inflater.inflate(layoutId, container, false));
    }

    @NonNull
    public static XHolder from(@NonNull Fragment fragment) {
        return new XHolder(Utils.requireNonNull(fragment.getView()));
    }

    @NonNull
    public static XHolder from(@NonNull Activity activity) {
        return new XHolder(activity.getWindow().getDecorView());
    }

    @NonNull
    public static XHolder from(@NonNull View view) {
        return new XHolder(Utils.requireNonNull(view));
    }

    protected XHolder(@NonNull View root) {
        super(root);
    }
}
