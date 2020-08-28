package cc.colorcat.xarchitecture.sample;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import x.common.component.annotation.ApiModel;
import x.common.component.network.VCall;

/**
 * Author: cxx
 * Date: 2020-08-28
 * GitHub: https://github.com/ccolorcat
 */
@ApiModel(baseUrl = "https://passport.teammix.com")
public interface AccountService {

    @FormUrlEncoded
    @POST("/connect/token")
    VCall<Map<String, Object>> loginWithPassword(
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("scope") String scope
    );
}
