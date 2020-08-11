package x.common.view;

import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public abstract class PopupWindowShowable<T extends PopupWindow> implements Showable {
    private final T mPopupWindow;

    public PopupWindowShowable(@NonNull T popupWindow) {
        mPopupWindow = Utils.requireNonNull(popupWindow, "popupWindow == null");
    }

    @Override
    public abstract void show();

    @Override
    public void dismiss() {
        mPopupWindow.dismiss();
    }

    @Override
    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    @NonNull
    public T getPopupWindow() {
        return mPopupWindow;
    }
}
