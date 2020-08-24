package cc.colorcat.xarchitecture.sample;

import retrofit2.http.GET;
import retrofit2.http.Query;
import x.common.component.annotation.ApiModel;
import x.common.component.network.VCall;

/**
 * Author: cxx
 * Date: 2020-08-21
 * GitHub: https://github.com/ccolorcat
 */
@ApiModel(baseUrl = "http://www.imooc.com/")
public interface TestMoocApi {

    @GET("/api/teacher")
    VCall<String> listCourses(@Query("type") int type, @Query("num") int number);
}
