package x.common.component.monitor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import x.common.IClient;
import x.common.component.XObservableImpl;

/**
 * Author: cxx
 * Date: 2020-08-05
 * GitHub: https://github.com/ccolorcat
 */
abstract class BaseNetworkMonitor extends XObservableImpl<Boolean> implements NetworkMonitor {
    BaseNetworkMonitor(@NonNull IClient client) {
        this.value = isNetworkAvailable(client.asAppClient().getApplication());
    }

    @Override
    public final boolean isAvailable() {
        return this.value;
    }

    @SuppressWarnings("deprecation")
    static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    // 此方法在判断蜂窝网络时不准
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isNetworkAvailableAtLeastM(ConnectivityManager manager) {
        Network network = manager.getActiveNetwork();
        NetworkCapabilities nc = manager.getNetworkCapabilities(network);
        return nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}
