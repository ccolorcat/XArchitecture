package x.common.component.core;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-22
 * GitHub: https://github.com/ccolorcat
 */
public interface AppStateListener {
    void onAppStateChanged(@NonNull AppState state);
}
