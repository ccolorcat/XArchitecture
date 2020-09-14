package x.test;

import androidx.annotation.NonNull;

import x.common.component.core.ClientInfo;
import x.common.component.core.ClientInfoProvider;

/**
 * Author: cxx
 * Date: 2020-09-14
 * GitHub: https://github.com/ccolorcat
 */
class TestClientInfoProvider implements ClientInfoProvider {
    @NonNull
    @Override
    public ClientInfo getClientInfo() {
        return new ClientInfo(
                "2.0.2",
                "99000855677029",
                "Xiaomi",
                "MI 5",
                "Android 8.0.0",
                "MI_CX",
                "phone"
        );
    }
}
