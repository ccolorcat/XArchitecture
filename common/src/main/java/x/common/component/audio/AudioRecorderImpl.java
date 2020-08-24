package x.common.component.audio;

import android.media.MediaRecorder;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.XObservableImpl;
import x.common.component.schedule.BackgroundXScheduler;
import x.common.component.schedule.MainXScheduler;
import x.common.util.FakeOptional;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-09
 * GitHub: https://github.com/ccolorcat
 */
final class AudioRecorderImpl extends XObservableImpl<Integer> implements AudioRecorder {
    private FakeOptional<MediaRecorder> recorder;
    private final Lazy<BackgroundXScheduler> scheduler = Lazy.by(() -> Hummingbird.visit(BackgroundXScheduler.class));
    private final Lazy<LinkedHashSet<StateListener>> listeners = Lazy.by(LinkedHashSet::new);
    private final Runnable volumeTask = this::updateVolume;

    private Future<?> future;
    private File savePath;
    private State mState;

    AudioRecorderImpl() {
        super(Hummingbird.visit(MainXScheduler.class));
    }

    @Override
    public boolean prepare(@NonNull File save) {
        savePath = Utils.requireNonNull(save, "save == null");
        if (recorder == null) recorder = FakeOptional.of(new MediaRecorder());
        boolean result = recorder.safeIfPresent((it) -> {
            // 录制的音频通道数
            it.setAudioChannels(1);
            // 设置音频采集方式（麦克风）
            it.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置文件的输出格式
            it.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
//            MediaRecorder.OutputFormat.DEFAULT
            //设置audio的编码格式
            it.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            it.setOutputFile(savePath.getAbsolutePath());
            it.prepare();
        });
        if (result) { notifyStateChanged(State.PREPARED); } else { savePath = null;}
        return result;
    }

    @Override
    public boolean start() {
        if (recorder.safeIfPresent(MediaRecorder::start)) {
            notifyStateChanged(State.STARTED);
            future = scheduler.get().scheduleWithFixedDelay(volumeTask, 0L, 200L, TimeUnit.MILLISECONDS);
            return true;
        }
        recorder.safeIfPresent((it) -> {
            it.stop();
            it.reset();
        });
        FakeOptional.ofNullable(savePath).safeIfPresent(File::deleteOnExit);
        savePath = null;
        return false;
    }

    @Override
    public boolean tryPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && recorder.safeIfPresent(MediaRecorder::pause)) {
            notifyStateChanged(State.PAUSED);
            return true;
        }
        return false;
    }

    @Override
    public boolean tryResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && recorder.safeIfPresent(MediaRecorder::resume)) {
            notifyStateChanged(State.RESUMED);
            return true;
        }
        return false;
    }

    @Override
    public boolean stop() {
        boolean result;
        if (result = recorder.safeIfPresent(MediaRecorder::stop)) {
            notifyStateChanged(State.STOPPED);
        }
        removeVolumeTask();
        return result;
    }

    @Override
    public boolean reset() {
        savePath = null;
        if (recorder.safeIfPresent(MediaRecorder::reset)) {
            notifyStateChanged(State.RESET);
            return true;
        }
        return false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Override
    public void release() {
        savePath = null;
        mState = null;
        recorder.safeIfPresent(MediaRecorder::release);
        recorder = null;
        if (listeners.initialized()) listeners.get().clear();
        removeVolumeTask();
    }

    @Override
    public boolean success() {
        return savePath != null && savePath.exists();
    }

    @Override
    public File getRecorded() {
        return savePath;
    }

    @Override
    public State getState() {
        return mState;
    }

    @Override
    public boolean addStateListener(@NonNull StateListener listener) {
        return this.listeners.get().add(Utils.requireNonNull(listener, "listener == null"));
    }

    @Override
    public boolean removeStateListener(StateListener listener) {
        return listener != null && this.listeners.get().remove(listener);
    }

    private void notifyStateChanged(@NonNull State state) {
        mState = state;
        for (StateListener listener : this.listeners.get()) {
            listener.onStateChanged(this, state);
        }
    }

    private void removeVolumeTask() {
        if (future instanceof Runnable) {
            scheduler.get().remove((Runnable) future);
            future = null;
        }
    }

    private void updateVolume() {
        update(recorder.get().getMaxAmplitude());
    }
}
