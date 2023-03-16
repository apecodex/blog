package cn.apecode.utils;

import cn.apecode.dto.UserDetailsDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @description: 用户工具类
 * @author: apecode
 * @date: 2022-05-28 16:08
 **/
@Component
public class UserUtils {

    /**
     * @description: 获取当前登录用户
     * @param
     * @return {@link UserDetailsDto}
     * @auther apecode
     * @date 2022/5/28 16:08
    */
    public static UserDetailsDto getLoginUser() {
        return (UserDetailsDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
