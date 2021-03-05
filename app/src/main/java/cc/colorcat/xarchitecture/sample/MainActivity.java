package cc.colorcat.xarchitecture.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.audio.AudioRecorder;
import x.common.component.core.LocationCore;
import x.common.component.loader.ObjectLoader;
import x.common.component.log.Logger;
import x.common.component.runtime.PermissionsHandler;
import x.common.component.runtime.RuntimeFor;
import x.common.view.BaseActivity;
import x.common.view.XHolder;

public class MainActivity extends BaseActivity {
    private final Logger mLogger = Logger.getLogger(this);

    private XHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHolder = XHolder.from(this).batchClick(
                mClick,
                R.id.btn_test_1,
                R.id.btn_test_2,
                R.id.btn_test_3
        );
        ObjectLoader.with(this)
//                .load("https://i0.hdslb.com/bfs/album/8d5baf53a12b8c2ac81dcb8e28385b79cb07459a.jpg")
                .load("https://up.enterdesk.com/edpic_source/b4/71/23/b47123e5f5ae76ae39b2f009e840226c.jpg")
                .asImage()
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .into(mHolder.get(R.id.iv_avatar));
    }

    private final View.OnClickListener mClick = v -> {
        int id = v.getId();
        if (id == R.id.btn_test_1) {
            test1();
        } else if (id == R.id.btn_test_2) {
            test2();
        } else if (id == R.id.btn_test_3) {
            test3();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void test1() {
        Picker.withGeneral(this)
                .pick(new Picker.Callback() {
                    @Override
                    public void onSucceed(@NonNull File file) {
                        Logger.getLogger("Picker").d("onResult.file=" + file);
                        ObjectLoader.with(MainActivity.this)
                                .load(file)
                                .asImage()
                                .circleCrop()
                                .into(mHolder.get(R.id.iv_avatar));
                    }

                    @Override
                    public void onFailed(@NonNull Throwable throwable) {
                        Logger.getLogger("Picker").d("onFailed=" + throwable);
                    }
                });
    }

    private Lazy<AudioRecorder> recorder = Lazy.by(() -> {
        AudioRecorder recorder = Hummingbird.visit(AudioRecorder.class);
        MainActivity.this.getLifecycle().addObserver(recorder);
        return recorder;
    });

    private void test2() {
        RuntimeFor.once(this)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .request(new PermissionsHandler() {
                    @Override
                    public void onAllGranted(@NonNull String[] permissions) {
                        Hummingbird.visit(LocationCore.class).start();
                    }
                });
    }

    private void test3() {
//        AudioRecorder recorder = this.recorder.get();
//        recorder.stop();
//        Logger.getLogger("AudioRecorder").v("success: %b, path: %s", recorder.success(), recorder.getRecorded());
//        recorder.reset();
        startActivity(new Intent(this, PhotosActivity.class));
    }
}