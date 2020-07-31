package x.common.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import x.common.component.Producer;
import x.common.component.annotation.Stateful;

/**
 * Author: cxx
 * Date: 2020-07-29
 * GitHub: https://github.com/ccolorcat
 */
@Stateful(ShowableControllerImpl.class)
public interface ShowableController extends LifecycleObserver {
    boolean show(@NonNull String key);

    boolean show(@NonNull String key, Producer<? extends Showable> producer);

    void dismiss(@NonNull String key);

    void dismiss(@NonNull String key, boolean remove);

    boolean isShowing(@NonNull String key);

    boolean isAnyShowing();
}
