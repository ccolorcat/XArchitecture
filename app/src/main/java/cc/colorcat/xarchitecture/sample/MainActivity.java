package cc.colorcat.xarchitecture.sample;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cc.colorcat.adapter.ViewHolder;
import cc.colorcat.login.LoginNavigation;
import x.common.component.Hummingbird;
import x.common.component.loader.ObjectLoader;
import x.common.component.log.Logger;
import x.common.component.schedule.IoXScheduler;
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
        ViewHolder holder = XHolder.from(this).batchClick(
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
        Hummingbird.visit(LoginNavigation.class).launch(this);
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

    private Future<?> mFuture;

    private Runnable mRunnable = new Runnable() {
        private int count = 0;

        @Override
        public void run() {
            mLogger.v("XScheduler: %d, %s, %b", count++, Thread.currentThread(), (Looper.getMainLooper() == Looper.myLooper()));
        }
    };

    private void test2() {
        mFuture = Hummingbird.visit(IoXScheduler.class)
                .scheduleWithFixedDelay(mRunnable, 2, 1, TimeUnit.SECONDS);
    }

    private void test3() {
        Hummingbird.visit(IoXScheduler.class).remove((Runnable) mFuture);
    }
}