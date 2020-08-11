package x.common.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public abstract class DialogFragmentShowable<T extends DialogFragment> implements Showable {
    private final T mDialogFragment;

    public DialogFragmentShowable(@NonNull T dialogFragment) {
        mDialogFragment = Utils.requireNonNull(dialogFragment, "dialogFragment == null");
    }

    @Override
    public abstract void show();

    @Override
    public void dismiss() {
        mDialogFragment.dismiss();
    }

    @Override
    public boolean isShowing() {
        return mDialogFragment.requireDialog().isShowing();
    }

    @NonNull
    public T getDialogFragment() {
        return mDialogFragment;
    }
}
