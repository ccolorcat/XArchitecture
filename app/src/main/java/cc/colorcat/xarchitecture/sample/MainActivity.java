package cc.colorcat.xarchitecture.sample;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import java.util.concurrent.TimeUnit;

import cc.colorcat.login.LoginNavigation;
import x.common.component.Hummingbird;
import x.common.component.log.Logger;
import x.common.component.schedule.BackgroundHandlerXScheduler;
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
        XHolder.from(this).batchClick(
                mClick,
                R.id.btn_test_1,
                R.id.btn_test_2,
                R.id.btn_test_3
        );
    }

    @Override
    protected void onDestroy() {
        Hummingbird.visit(BackgroundHandlerXScheduler.class)
                .shutdown();
        super.onDestroy();
    }

    private void test1() {
        Hummingbird.visit(LoginNavigation.class).launch(MainActivity.this);
    }

    private void test2() {
        Hummingbird.visit(BackgroundHandlerXScheduler.class)
                .scheduleWithFixedDelay(new Runnable() {
                    private int count = 0;

                    @Override
                    public void run() {
                        mLogger.v("BackgroundHandlerXScheduler: %d, %s, %b", count++, Thread.currentThread(), (Looper.getMainLooper() == Looper.myLooper()));
                    }
                }, 2, 3, TimeUnit.SECONDS);
    }

    private void test3() {
        PersonStore store = Hummingbird.visit(PersonStore.class);
        Person person = new Person("orange", 2);
        store.save(person);
        mLogger.v("clear: " + store.clear());
        mLogger.v("read: " + store.read());
        mLogger.v("remove: " + store.remove());
    }
}