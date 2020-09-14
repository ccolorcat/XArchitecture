package x.common.component.core;

import androidx.annotation.NonNull;

import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-09-14
 * GitHub: https://github.com/ccolorcat
 */
@Core(ClientInfoProviderImpl.class)
public interface ClientInfoProvider {
    @NonNull
    ClientInfo getClientInfo();
}
