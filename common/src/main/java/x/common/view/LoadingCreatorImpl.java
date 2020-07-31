package x.common.view;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
public class LoadingCreatorImpl implements LoadingCreator {
    @NonNull
    @Override
    public Showable create(@NonNull Context context) {
        // TODO: 2020/7/29
        ProgressDialog dialog = new ProgressDialog(context);
        return new DialogShowable(dialog);
    }
}
