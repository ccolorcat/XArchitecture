package cc.colorcat.xarchitecture.sample;

import android.os.Bundle;
import android.view.View;

import java.util.stream.Stream;

import cc.colorcat.login.LoginNavigation;
import x.common.component.Hummingbird;
import x.common.view.BaseActivity;
import x.common.view.XHolder;

public class MainActivity extends BaseActivity {
    private View.OnClickListener mClick = v -> {
        switch (v.getId()) {
            case R.id.btn_test_1:
                Hummingbird.visit(LoginNavigation.class).launch(MainActivity.this);
                break;
            case R.id.btn_test_2:
                test2();
                break;
            case R.id.btn_test_3:
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

    private void test2() {
        Stream.of("this", "is", "a", "test")
                .mapToInt(String::length)
                .forEach(System.out::println);
    }
}