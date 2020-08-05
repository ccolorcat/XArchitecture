package x.common.component.monitor;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import x.common.IClient;
import x.common.util.AndroidUtils;


/**
 * Author: cxx
 * Date: 2020-04-17
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
final class NetworkMonitorLollipop extends BaseNetworkMonitor {
    private Network mCurrentNetwork;
    private ConnectivityManager mManager;

    NetworkMonitorLollipop(@NonNull IClient client) {
        super(client);
        mManager = AndroidUtils.getSystemService(client.asAppClient().getApplication(), ConnectivityManager.class);
        mManager.registerNetworkCallback(new NetworkRequest.Builder().build(), new NetworkCallback());
    }

    private class NetworkCallback extends ConnectivityManager.NetworkCallback {
        @Override
        public void onAvailable(@NonNull android.net.Network network) {
            super.onAvailable(network);
            mCurrentNetwork = network;
            update(true);
        }

        @Override
        public void onLost(@NonNull android.net.Network network) {
            super.onLost(network);
            if (network.equals(mCurrentNetwork)) {
                update(false);
            }
        }
    }
}
