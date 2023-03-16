package cn.apecode.common.annotation;

import java.lang.annotation.*;

/**
 * @description: 操作日志注解
 * @author: apecode
 * @date: 2022-07-09 12:13
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     *
     * @description: 操作类型
     *
     * @return String
     * @auther apecode
     * @date 2022-07-09 12:15
     */
    String optType() default "";

}
