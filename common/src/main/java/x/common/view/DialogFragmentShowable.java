package x.common.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public abstract class DialogFragmentShowable implements Showable {
    private final DialogFragment mDialogFragment;

    public DialogFragmentShowable(@NonNull DialogFragment dialogFragment) {
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
}
