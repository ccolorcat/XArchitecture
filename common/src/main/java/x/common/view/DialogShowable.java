package x.common.view;

import android.app.Dialog;

import androidx.annotation.NonNull;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public class DialogShowable implements Showable {
    private final Dialog mDialog;

    public DialogShowable(@NonNull Dialog dialog) {
        mDialog = Utils.requireNonNull(dialog, "dialog == null");
    }

    @Override
    public void show() {
        mDialog.show();
    }

    @Override
    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public boolean isShowing() {
        return mDialog.isShowing();
    }
}
