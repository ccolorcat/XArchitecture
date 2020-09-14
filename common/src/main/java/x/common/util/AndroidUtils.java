package x.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Author: cxx
 * Date: 2020-07-13
 * GitHub: https://github.com/ccolorcat
 */
public class AndroidUtils {
    public static boolean safeStartActivity(@NonNull Context context, @NonNull Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            if (!(context instanceof Activity)) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean safeStartActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode) {
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    @NonNull
    public static Uri toUri(@NonNull URI uri) {
        return Uri.parse(uri.toString());
    }

    public static <T> T getSystemService(@NonNull Context context, Class<T> serviceClass) {
        return ContextCompat.getSystemService(context, serviceClass);
    }

    public static void configAlarmManager(AlarmManager manager, PendingIntent pi, long delay, TimeUnit unit) {
        delay = unit.toMillis(delay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pi);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + delay, pi);
            manager.setAlarmClock(info, pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
        }
    }

    public static boolean isTablet(@NonNull Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @SuppressLint("HardwareIds")
    @NonNull
    public static String getDeviceId(@NonNull Context context) {
        ContentResolver resolver = context.getContentResolver();
        String androidId = Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
        return Utils.nullElse(androidId, "");
    }

    @NonNull
    public static String getDeviceName(@NonNull Context context) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null ? Utils.nullElse(adapter.getName(), "") : "";
    }

    private AndroidUtils() {
        throw new AssertionError("no instance");
    }
}
