package club.anlan.lanlife.base.annotation;

import club.anlan.lanlife.base.enums.BusinessType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/4 13:28
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 描述
     */
    String description();

    /**
     * 业务操作类型
     */
    BusinessType businessType() default BusinessType.QUERY;

    /**
     * 是否保存日志
     */
    boolean saveToDb() default false;
}

