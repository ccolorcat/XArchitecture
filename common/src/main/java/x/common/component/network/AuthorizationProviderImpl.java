package x.common.component.network;


import x.common.IClient;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class AuthorizationProviderImpl implements AuthorizationProvider {
    private AuthorizationProviderImpl(IClient client) {
    }

    @Override
    public String getAuthorization() {
        return "empty";
    }
}
