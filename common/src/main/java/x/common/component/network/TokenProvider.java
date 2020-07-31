package x.common.component.network;


import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
@Core(TokenProviderImpl.class)
public interface TokenProvider {
    String getToken();
}
