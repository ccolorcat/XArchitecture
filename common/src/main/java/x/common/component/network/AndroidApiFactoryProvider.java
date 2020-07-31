package x.common.component.network;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.Cache;
import x.common.IAppClient;
import x.common.IClient;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class AndroidApiFactoryProvider extends ApiFactoryProviderImpl {
    AndroidApiFactoryProvider(@NonNull IClient client) {
        super(client);
    }

    @Override
    protected Cache makeCache(IClient client) {
        IAppClient androidClient = (IAppClient) client;
        File cacheDir = new File(androidClient.getApplication().getCacheDir(), "netCache");
        if (cacheDir.exists() || cacheDir.mkdirs()) {
            return new Cache(cacheDir, 1024 * 1024 * 50);
        }
        return null;
    }
}
