package x.common.test;

import androidx.annotation.NonNull;

import java.io.File;

import x.common.IAppClient;
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
    public File getCacheDir() {
        return new File(System.getProperty("user.home"), "cache");
    }

    @Override
    public boolean isTest() {
        return true;
    }

    @Override
    public IAppClient asAppClient() {
        throw new UnsupportedOperationException("do not cast to IAppClient");
    }
}
