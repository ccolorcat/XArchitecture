package cc.colorcat.login.model;

import java.util.List;

import cc.colorcat.login.entity.Course;
import retrofit2.http.GET;
import retrofit2.http.Query;
import x.common.component.annotation.ApiModel;
import x.common.component.network.VCall;
import x.common.entity.Result;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
@ApiModel(baseUrl = "http://www.imooc.com/")
public interface LoginModel {
    @GET("/api/teacher")
    VCall<Result<List<Course>>> listCourses(@Query("type") int type, @Query("num") int number);

    @GET("/api/teacher")
    VCall<String> listCoursesAsString(@Query("type") int type, @Query("num") int number);
}
