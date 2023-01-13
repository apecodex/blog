package cn.apecode.blog.service;

import cn.apecode.blog.dto.UserInfoBackDto;
import cn.apecode.blog.dto.UserInfoDto;
import cn.apecode.blog.entity.UserAuth;
import cn.apecode.blog.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户账号 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface UserAuthService extends IService<UserAuth> {

    /**
     * @description: 获取后台用户列表
     * @param conditionVo
     * @return {@link PageResult<UserInfoBackDto>}
     * @auther apecode
     * @date 2022/6/9 11:55
    */
    PageResult<UserInfoBackDto> listUserInfoBack(ConditionVo conditionVo);

    /**
     * @description: 修改密码
     * @param password
     * @auther apecode
     * @date 2022/6/9 15:36
    */
    void updatePassword(PasswordVo password);

    /**
     * @description: 修改用户密码
     * @param findPassword
     * @auther apecode
     * @date 2022/6/9 21:28
    */
    void findPassword(FindPasswordVo findPassword);

    /**
     * @description: 用户注册
     * @param register
     * @auther apecode
     * @date 2022/6/9 23:13
    */
    void register(RegisterVo register);

    /**
     * @description: 发送邮箱验证码
     * @param email
     * @auther apecode
     * @date 2022/6/10 20:18
    */
    void sendCode(String email);

    /**
     * @description: 修改用户禁用状态
     * @param userEnable
     * @auther apecode
     * @date 2022/6/14 21:23
    */
    void updateUserEnable(UserEnableVo userEnable);

    /**
     * @description: 第三方QQ登录
     * @param qqLoginVo
     * @return {@link UserInfoDto}
     * @auther apecode
     * @date 2023/1/5 12:31
    */
    UserInfoDto qqLogin(QQLoginVo qqLoginVo);
}
