package x.common.component.core;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import x.common.BuildConfig;
import x.common.IClient;
import x.common.util.AndroidUtils;

/**
 * Author: cxx
 * Date: 2020-09-14
 * GitHub: https://github.com/ccolorcat
 */
final class ClientInfoProviderImpl implements ClientInfoProvider {
    private final ClientInfo info;

    private ClientInfoProviderImpl(@NonNull IClient client) {
        Context context = client.asAppClient().getApplication();
        info = new ClientInfo(
                BuildConfig.VERSION_NAME,
                AndroidUtils.getDeviceId(context),
                Build.BRAND,
                Build.MODEL,
                Build.VERSION.RELEASE,
                AndroidUtils.getDeviceName(context),
                AndroidUtils.isTablet(context) ? "pad" : "phone"
        );
    }

    @NonNull
    @Override
    public ClientInfo getClientInfo() {
        return info;
    }
}
