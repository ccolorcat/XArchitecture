package x.common.component;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author: cxx
 * Date: 2020-07-21
 * GitHub: https://github.com/ccolorcat
 */
public abstract class SimpleActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) { }

    @Override
    public void onActivityStarted(@NonNull Activity activity) { }

    @Override
    public void onActivityResumed(@NonNull Activity activity) { }

    @Override
    public void onActivityPaused(@NonNull Activity activity) { }

    @Override
    public void onActivityStopped(@NonNull Activity activity) { }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) { }
}
