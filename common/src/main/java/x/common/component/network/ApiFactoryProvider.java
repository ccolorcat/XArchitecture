package x.common.component.network;

import androidx.annotation.NonNull;

import x.common.component.annotation.Core;


/**
 * Author: cxx
 * Date: 2020-06-30
 * GitHub: https://github.com/ccolorcat
 */
@Core(AndroidApiFactoryProvider.class)
public interface ApiFactoryProvider {
    @NonNull
    ApiFactory of(@NonNull String baseUrl);
}
