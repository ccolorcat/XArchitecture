package x.common;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import x.common.component.annotation.ApiModel;
import x.common.component.network.Sign;
import x.common.component.network.SignType;
import x.common.component.network.VCall;

/**
 * Author: cxx
 * Date: 2020-08-21
 * GitHub: https://github.com/ccolorcat
 */
@ApiModel(baseUrl = "http://www.imooc.com/")
public interface TestMoocApi {

    @Sign(SignType.USER)
    @GET("/api/teacher")
    VCall<String> listCourses(@Query("type") int type, @Query("num") int number);

    @Sign(SignType.ORG)
    @GET("/{api}/{teacher}")
    VCall<String> testCourses(@Path("api") String api, @Path("teacher") String teacher, @Query("type") int type, @Query("num") int number);

    @GET
    VCall<String> getCourses(@Url String url, @Query("type") int type, @Query("num") int number);
}
