package x.common.component.core;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-21
 * GitHub: https://github.com/ccolorcat
 */
public interface AccountStateListener {
    void onAccountStateChanged(@NonNull AccountState state);
}
