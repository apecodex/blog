package cn.apecode.service;

import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.dto.UserInfoFrontDto;
import cn.apecode.entity.UserInfo;
import cn.apecode.vo.QQLoginVo;
import cn.apecode.vo.SaveOrUnbindEmailVo;
import cn.apecode.vo.UserInfoVo;
import cn.apecode.vo.UserRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * @param userRole
     * @description: 修改用户角色
     * @auther apecode
     * @date 2022/6/14 22:53
     */
    void updateUserRole(UserRoleVo userRole);

    /**
     * @param file
     * @return {@link UploadFileInfoDto}
     * @description: 更新用户头像
     * @auther apecode
     * @date 2022/6/15 0:00
     */
    UploadFileInfoDto updateUserAvatar(MultipartFile file);

    /**
     * @param saveEmail
     * @return {@link String}
     * @description: 绑定用户邮箱
     * @auther apecode
     * @date 2022/6/15 14:28
     */
    String saveUserEmail(SaveOrUnbindEmailVo saveEmail);

    /**
     * @param userInfo
     * @description: 更新用户信息
     * @auther apecode
     * @date 2022/6/15 16:51
     */
    void updateUserInfo(UserInfoVo userInfo);

    /**
     * @return {@link UserInfoFrontDto}
     * @description: 获取用户信息
     * @auther apecode
     * @date 2022/6/15 20:46
     */
    UserInfoFrontDto getUserInfo();

    /**
     * @param unbindEmailVo
     * @description: 解绑用户邮箱
     * @auther apecode
     * @date 2023/1/6 1:31
     */
    String unbindEmail(SaveOrUnbindEmailVo unbindEmailVo);

    /**
     * @param qqLoginVo
     * @description: 绑定QQ
     * @auther apecode
     * @date 2023/1/6 11:15
     */
    void bindQQ(QQLoginVo qqLoginVo);

    /**
     * @description: 解绑QQ
     * @auther apecode
     * @date 2023/1/6 11:41
     */
    void unbindQQ();
}
