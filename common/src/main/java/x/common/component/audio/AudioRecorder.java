package x.common.component.audio;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.io.File;

import x.common.component.XObservable;
import x.common.component.annotation.Stateful;

/**
 * Author: cxx
 * Date: 2020-08-09
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Stateful(AudioRecorderImpl.class)
public interface AudioRecorder extends XObservable<Integer>, LifecycleObserver {
    boolean prepare(@NonNull File save);

    boolean start();

    boolean tryPause();

    boolean tryResume();

    boolean stop();

    boolean reset();

    void release();

    boolean success();

    File getRecorded();

    State getState();

    default void bindStateListener(@NonNull LifecycleOwner owner, @NonNull StateListener listener) {
        owner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            switch (event) {
                case ON_CREATE:
                    addStateListener(listener);
                    break;
                case ON_DESTROY:
                    removeStateListener(listener);
                    break;
                default:
                    break;
            }
        });
    }

    boolean addStateListener(@NonNull StateListener listener);

    boolean removeStateListener(StateListener listener);

    interface StateListener {
        void onStateChanged(@NonNull AudioRecorder recorder, @NonNull State state);
    }

    enum State {
        PREPARED,
        STARTED,
        RESUMED,
        PAUSED,
        STOPPED,
        RESET
    }
}
