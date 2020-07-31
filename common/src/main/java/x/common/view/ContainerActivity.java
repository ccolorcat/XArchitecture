package x.common.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.common.R;
import x.common.util.AndroidUtils;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public class ContainerActivity extends BaseActivity {
    private static final String KEY_FRAGMENT_CLASS = "class.fragment.key";
    private static final String KEY_FRAGMENT_ARGS = "args.fragment.key";

    public static void launchFragment(@NonNull Context context, @NonNull Class<? extends BaseFragment> clazz) {
        launchFragment(context, clazz, null);
    }

    public static void launchFragment(@NonNull Context context, @NonNull Class<? extends BaseFragment> clazz, Bundle args) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra(KEY_FRAGMENT_CLASS, Utils.requireNonNull(clazz, "clazz == null"));
        if (args != null) intent.putExtra(KEY_FRAGMENT_ARGS, args);
        AndroidUtils.safeStartActivity(context, intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Intent intent = getIntent();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, getFragmentClass(intent), getArgs(intent), "tag_fragment_container")
                .commit();
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends BaseFragment> getFragmentClass(Intent intent) {
        return (Class<? extends BaseFragment>) Utils.requireNonNull(intent.getSerializableExtra(KEY_FRAGMENT_CLASS));
    }

    private static Bundle getArgs(Intent intent) {
        return intent.getBundleExtra(KEY_FRAGMENT_ARGS);
    }

    private BaseFragment create() {
        try {
            Intent intent = getIntent();
            Class<?> clazz = (Class<?>) intent.getSerializableExtra(KEY_FRAGMENT_CLASS);
            Utils.requireNonNull(clazz, "missing class by key " + KEY_FRAGMENT_CLASS);
            BaseFragment fragment = (BaseFragment) clazz.newInstance();
            Bundle args = intent.getBundleExtra(KEY_FRAGMENT_ARGS);
            if (args != null) fragment.setArguments(args);
            return fragment;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
