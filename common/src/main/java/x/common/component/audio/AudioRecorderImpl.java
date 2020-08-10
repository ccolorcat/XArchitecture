package x.common.component.audio;

import android.media.MediaRecorder;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.XObservableImpl;
import x.common.component.log.Logger;
import x.common.component.schedule.BackgroundXScheduler;
import x.common.component.schedule.MainXScheduler;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-09
 * GitHub: https://github.com/ccolorcat
 */
final class AudioRecorderImpl extends XObservableImpl<Integer> implements AudioRecorder {
    private final Lazy<MediaRecorder> recorder = Lazy.by(MediaRecorder::new);
    private final Lazy<BackgroundXScheduler> scheduler = Lazy.by(() -> Hummingbird.visit(BackgroundXScheduler.class));
    private final Runnable volumeTask = this::updateVolume;

    private Future<?> future;
    private File savePath;

    AudioRecorderImpl() {
        super(Hummingbird.visit(MainXScheduler.class));
    }

    @Override
    public boolean prepare(@NonNull File save) {
        try {
            this.savePath = Utils.requireNonNull(save, "save == null");
//            savePath = new File(client.cacheDir(), System.currentTimeMillis() + ".aac");
            MediaRecorder recorder = this.recorder.get();
            // 录制的音频通道数
            recorder.setAudioChannels(1);
            // 设置音频采集方式（麦克风）
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置文件的输出格式
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
//            MediaRecorder.OutputFormat.DEFAULT
            //设置audio的编码格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(savePath.getAbsolutePath());
            recorder.prepare();
            return true;
        } catch (Throwable t) {
            Logger.getDefault().e(t);
            return false;
        }
    }

    @Override
    public void start() {
        try {
            MediaRecorder recorder = this.recorder.get();
            recorder.start();
            future = scheduler.get().scheduleWithFixedDelay(volumeTask, 0L, 200L, TimeUnit.MILLISECONDS);
        } catch (Throwable t) {
            Logger.getDefault().e(t);
            stop();
            if (savePath != null) savePath.deleteOnExit();
            reset();
        }
    }

    @Override
    public void stop() {
        try {
            if (future instanceof Runnable) {
                scheduler.get().remove((Runnable) future);
                future = null;
            }
            recorder.get().stop();
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
    }

    @Override
    public void reset() {
        recorder.get().reset();
        savePath = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Override
    public void release() {
        Logger.getDefault().w("AudioRecorderImpl release");
        if (future instanceof Runnable) {
            scheduler.get().remove((Runnable) future);
        }
        savePath = null;
        this.recorder.get().release();
    }

    @Override
    public boolean success() {
        return savePath != null && savePath.exists();
    }

    @Override
    public File getRecorded() {
        return savePath;
    }

    private void updateVolume() {
        update(recorder.get().getMaxAmplitude());
    }
}
