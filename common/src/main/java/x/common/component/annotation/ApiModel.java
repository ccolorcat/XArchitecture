package x.common.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * stateless
 * <p>
 * Author: cxx
 * Date: 2020-07-23
 * GitHub: https://github.com/ccolorcat
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModel {
    Class<?> value() default Void.class;

    String baseUrl() default "";
}
