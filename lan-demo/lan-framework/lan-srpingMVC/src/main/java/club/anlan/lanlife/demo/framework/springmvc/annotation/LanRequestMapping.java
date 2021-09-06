package club.anlan.lanlife.demo.framework.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/6 23:16
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LanRequestMapping {

    String value() default "";
}
