package x.common.component.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: cxx
 * Date: 2020-08-25
 * GitHub: https://github.com/ccolorcat
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sign {
    SignType value() default SignType.ORG;
}
