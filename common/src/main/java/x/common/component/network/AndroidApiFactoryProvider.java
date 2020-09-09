package x.common.component.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @Nullable
    @Override
    protected Cache makeCache(IClient client) {
        IAppClient appClient = client.asAppClient();
        File cacheDir = new File(appClient.cacheDir(), "OkCache");
        if (cacheDir.exists() || cacheDir.mkdirs()) {
            return new Cache(cacheDir, 1024 * 1024 * 50);
        }
        return null;
    }
}
