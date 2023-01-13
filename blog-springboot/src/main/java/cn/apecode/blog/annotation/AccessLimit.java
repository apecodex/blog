package cn.apecode.blog.annotation;

import java.lang.annotation.*;

/**
 * @description: 接口限流
 * @author: apecode
 * @date: 2022-12-24 20:49
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    /**
     * 访问时间（秒）
     * @return
     */
    long seconds();

    /**
     * 最大请求次数
     * @return
     */
    int maxCount();
}
