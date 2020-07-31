package x.common.test;

import androidx.annotation.NonNull;

import x.common.IClient;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class TestClient implements IClient {
    @NonNull
    @Override
    public String getBaseUrl() {
        return "https://www.google.com/";
    }

    @Override
    public boolean isTest() {
        return true;
    }
}
