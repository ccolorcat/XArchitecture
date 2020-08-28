package x.common;

import retrofit2.http.GET;
import retrofit2.http.Query;
import x.common.component.annotation.ApiModel;
import x.common.component.network.Sign;
import x.common.component.network.SignType;
import x.common.component.network.VCall;

/**
 * Author: cxx
 * Date: 2020-08-07
 * GitHub: https://github.com/ccolorcat
 */
@ApiModel(baseUrl = "https://www.baidu.com/")
public interface TestApiModel {

    @Sign(SignType.ORG)
    @GET("/s")
    VCall<String> search(@Query("wd") String word);
}
