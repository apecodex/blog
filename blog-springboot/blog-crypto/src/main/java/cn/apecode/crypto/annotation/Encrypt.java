package cn.apecode.crypto.annotation;

import java.lang.annotation.*;

/**
 * @author apecode.
 * @description: 解密注解
 * @date 2024/1/18 22:56
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Encrypt {

}
