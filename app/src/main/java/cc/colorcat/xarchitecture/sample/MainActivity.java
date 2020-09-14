package cc.colorcat.xarchitecture.sample;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.audio.AudioRecorder;
import x.common.component.finder.FileOperator;
import x.common.component.finder.Filename;
import x.common.component.finder.FinderCore;
import x.common.component.loader.ObjectLoader;
import x.common.component.log.Logger;
import x.common.component.network.ApiCallback;
import x.common.component.network.ApiException;
import x.common.component.runtime.RuntimeFor;
import x.common.view.BaseActivity;
import x.common.view.XHolder;

public class MainActivity extends BaseActivity {
    private final Logger mLogger = Logger.getLogger(this);

    private View.OnClickListener mClick = v -> {
        switch (v.getId()) {
            case R.id.btn_test_1:
                test1();
                break;
            case R.id.btn_test_2:
                test2();
                break;
            case R.id.btn_test_3:
                test3();
                break;
            default:
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XHolder holder = XHolder.from(this).batchClick(
                mClick,
                R.id.btn_test_1,
                R.id.btn_test_2,
                R.id.btn_test_3
        );
        ObjectLoader.with(this)
                .load("https://i0.hdslb.com/bfs/album/8d5baf53a12b8c2ac81dcb8e28385b79cb07459a.jpg")
                .asImage()
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .into(holder.get(R.id.iv_avatar));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void test1() {
        Hummingbird.visit(TestMoocApi.class)
                .listCourses(4, 30)
                .enqueue(new ApiCallback<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(@NonNull String data) {

                    }

                    @Override
                    public void onFailure(@NonNull ApiException cause) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
//        Hummingbird.visit(LoginNavigation.class).launch(this);
//        Hummingbird.visit(NetworkMonitor.class)
//                .bind(true, this, (available) -> mLogger.d("network changed: " + available));
//        ObjectLoader.with(this)
//                .load("https://dldir1.qq.com/weixin/mac/WeChatMac.dmg")
//                .asDownloader()
//                .fetch(new DownloadListener() {
//                    @Override
//                    public void onProgress(long finished, long total, int percent) {
//                        mLogger.v("onProgress: %d, %d, %d", finished, total, percent);
//                    }
//
//                    @Override
//                    public void onSuccess(@NonNull FileOperator operator) {
//                        mLogger.v("onSuccess: " + operator.getUri());
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Throwable throwable) {
//                        mLogger.e(throwable);
//                    }
//                });
    }

    private Lazy<AudioRecorder> recorder = Lazy.by(() -> {
        AudioRecorder recorder = Hummingbird.visit(AudioRecorder.class);
        MainActivity.this.getLifecycle().addObserver(recorder);
        return recorder;
    });

    private void test2() {
        RuntimeFor.once(this)
                .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(permissions -> {
                    AudioRecorder core = recorder.get();
                    core.bind(MainActivity.this, value -> Logger.getLogger("AudioRecorder").v("volume: " + value));
                    FileOperator operator = Hummingbird.visit(FinderCore.class).requireFileOperator(
                            null,
                            null,
                            Filename.of(String.valueOf(System.currentTimeMillis()), "aac")
                    );
                    if (core.prepare(new File(operator.getUri().getPath()))) {
                        core.start();
                    }
                });
    }

    private void test3() {
        AudioRecorder recorder = this.recorder.get();
        recorder.stop();
        Logger.getLogger("AudioRecorder").v("success: %b, path: %s", recorder.success(), recorder.getRecorded());
        recorder.reset();
    }
}