package x.test;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * Author: cxx
 * Date: 2020-07-28
 * GitHub: https://github.com/ccolorcat
 */
public final class TestLifecycleOwner implements LifecycleOwner {
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return State.RESUMED;
            }
        };
    }
}
