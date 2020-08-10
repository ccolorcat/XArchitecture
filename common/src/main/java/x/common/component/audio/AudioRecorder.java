package x.common.component.audio;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import java.io.File;

import x.common.component.XObservable;
import x.common.component.annotation.Stateful;

/**
 * Author: cxx
 * Date: 2020-08-09
 * GitHub: https://github.com/ccolorcat
 */
@Stateful(AudioRecorderImpl.class)
public interface AudioRecorder extends XObservable<Integer>, LifecycleObserver {
    boolean prepare(@NonNull File save);

    void start();

    void stop();

    void reset();

    void release();

    boolean success();

    File getRecorded();
}
