package x.common;

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import x.common.component.annotation.ApiModel
import x.common.component.network.VCall

/**
 * Author: cxx
 * Date: 2020-08-28
 * GitHub: https://github.com/ccolorcat
 */
@ApiModel(baseUrl = "https://passport.teammix.com/")
interface AccountService {

    @FormUrlEncoded
    @POST("/connect/token")
    fun loginWithPassword(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("client_id") clientId: String = "teamix-app-android",
            @Field("client_secret") clientSecret: String = "123456",
            @Field("grant_type") grantType: String = "password",
            @Field("scope") scope: String = "openid offline_access workapps.client api.workapps.open api.workapps.user"
    ): VCall<Map<String, Any>>
}
