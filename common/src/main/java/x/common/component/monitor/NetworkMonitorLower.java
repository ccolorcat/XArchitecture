package x.common.component.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-04-17
 */
final class NetworkMonitorLower extends BaseNetworkMonitor {
    NetworkMonitorLower(@NonNull IClient client) {
        super(client);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        client.asAppClient().getApplication().registerReceiver(new NetworkReceiver(), filter);
    }

    class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean result = isNetworkAvailable(context);
            Logger.getDefault().v("NetworkMonitorLower.onReceive: " + result);
            update(isNetworkAvailable(context));
        }
    }
}
