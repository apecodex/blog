package cn.apecode.strategy;

import cn.apecode.dto.UserInfoDto;

/**
 * @description: 第三方登录策略
 * @author: apecode
 * @date: 2023-01-04 21:57
 **/
public interface SocialLoginStrategy {

    /**
     * 登录
     * @param data
     * @return {@link UserInfoDto}
     */
    UserInfoDto login(String data);
}
