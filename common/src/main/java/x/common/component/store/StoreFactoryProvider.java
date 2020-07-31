package x.common.component.store;

import androidx.annotation.NonNull;

import x.common.component.annotation.Core;


/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
@Core(SharedPreferencesStoreFactoryProvider.class)
public interface StoreFactoryProvider {
    @NonNull
    StoreFactory of(@NonNull String name);
}
