package x.common.component.store;

import android.content.Context;

import androidx.annotation.NonNull;

import x.common.IAppClient;
import x.common.IClient;


/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class SharedPreferencesStoreFactoryProvider extends BaseStoreFactoryProvider {
    private final Context context;

    private SharedPreferencesStoreFactoryProvider(@NonNull IClient client) {
        super(client);
        context = ((IAppClient) client).getApplication();
    }

    @NonNull
    @Override
    protected Store newStore(String name) {
        return new SharedPreferencesStore(context.getSharedPreferences(name, Context.MODE_PRIVATE));
    }
}
