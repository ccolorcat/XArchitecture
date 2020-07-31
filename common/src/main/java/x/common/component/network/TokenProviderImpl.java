package x.common.component.network;


import x.common.IClient;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class TokenProviderImpl implements TokenProvider {
    private TokenProviderImpl(IClient client) {

    }

    @Override
    public String getToken() {
        return "empty";
    }
}
