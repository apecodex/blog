package cn.apecode.service.impl;

import cn.apecode.utils.JwtUtils;
import cn.apecode.utils.QQTokenUtils;
import cn.apecode.utils.UserUtils;
import cn.apecode.common.enums.FilePathEnum;
import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.BeanCopyUtils;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.dto.UserDetailsDto;
import cn.apecode.dto.UserInfoFrontDto;
import cn.apecode.entity.LoginQq;
import cn.apecode.entity.UserAuth;
import cn.apecode.entity.UserInfo;
import cn.apecode.entity.UserRole;
import cn.apecode.service.RedisService;
import cn.apecode.mapper.LoginQqMapper;
import cn.apecode.mapper.UserAuthMapper;
import cn.apecode.mapper.UserInfoMapper;
import cn.apecode.service.UserInfoService;
import cn.apecode.service.UserRoleService;
import cn.apecode.strategy.context.UploadFileStrategyContext;
import cn.apecode.utils.AnjiCaptchaUtils;
import cn.apecode.vo.QQLoginVo;
import cn.apecode.vo.SaveOrUnbindEmailVo;
import cn.apecode.vo.UserInfoVo;
import cn.apecode.vo.UserRoleVo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.apecode.common.constant.RedisPrefixConst.USER_CACHE;
import static cn.apecode.common.constant.RedisPrefixConst.USER_CODE_KEY;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final UserRoleService userRoleService;
    private final UserAuthMapper userAuthMapper;
    private final UserInfoMapper userInfoMapper;
    private final RedisService redisService;
    private final UploadFileStrategyContext uploadFileStrategyContext;
    private final AnjiCaptchaUtils anjiCaptchaUtils;
    private final JwtUtils jwtUtils;
    private final LoginQqMapper loginQqMapper;
    private final QQTokenUtils qqTokenUtils;

    /**
     * @param userRole
     * @description: 修改用户角色
     * @auther apecode
     * @date 2022/6/14 22:53
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleVo userRole) {
        Integer userId = SecurityUtils.decrypt(userRole.getUserId());
        if (Objects.isNull(userId)) throw new BizException("用户id有误");
        if (UserUtils.getLoginUser().getId().equals(userId)) {
            throw new BizException("无法修改自己的角色");
        }
        UserAuth userAuth = userAuthMapper.selectOne(Wrappers.<UserAuth>lambdaQuery().select(UserAuth::getUsername).eq(UserAuth::getId, userId));
        if (Objects.isNull(userAuth)) {
            throw new BizException("用户id有误");
        }
        // 删除用户角色
        userRoleService.remove(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId, userId));
        // 重新添加
        List<UserRole> userRoles = userRole.getRoleList().stream().map(
                roleId -> {
                    Integer rId = SecurityUtils.decrypt(roleId);
                    if (Objects.isNull(rId)) throw new BizException("角色id有误");
                    return UserRole.builder()
                            .roleId(rId)
                            .userId(userId)
                            .updateTime(CommonUtils.getLocalDateTime())
                            .build();
                }).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        // 删除redis用户缓存，让其重新添加
        redisService.hDel(USER_CACHE, userAuth.getUsername());
    }

    /**
     * @param file
     * @return {@link UploadFileInfoDto}
     * @description: 更新用户头像
     * @auther apecode
     * @date 2022/6/15 0:00
     */
    @Override
    public UploadFileInfoDto updateUserAvatar(MultipartFile file) {
        if (file.getSize() > 3145728L) {
            throw new BizException("图片太大！请控制在3MB");
        }
        UploadFileInfoDto avatar = uploadFileStrategyContext.executeUploadFileStrategy(file, FilePathEnum.AVATAR.getPath());
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .avatar(avatar.getUrl())
                .build();
        userInfoMapper.updateById(userInfo);
        // 删除redis用户缓存，让其重新添加
        redisService.hDel(USER_CACHE, UserUtils.getLoginUser().getUsername());
        return avatar;
    }

    /**
     * @param saveEmail
     * @description: 绑定用户邮箱
     * @auther apecode
     * @date 2022/6/15 14:28
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveUserEmail(SaveOrUnbindEmailVo saveEmail) {
        checkCodeAndCaptcha(saveEmail);
        // 和原先一样的邮箱，就不用保存了
        if (Objects.nonNull(UserUtils.getLoginUser().getEmail()) && UserUtils.getLoginUser().getEmail().equals(saveEmail.getEmail())) {
            throw new BizException("该邮箱已绑定，无须重复绑定");
        }
        // 判断邮箱是否存在
        UserInfo info = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery()
                .select(UserInfo::getEmail).eq(UserInfo::getEmail, saveEmail.getEmail()));
        if (Objects.nonNull(info)) {
            throw new BizException("此邮箱已被绑定");
        }
        // 更新用户邮箱
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .email(saveEmail.getEmail())
                .build();
        userInfoMapper.updateById(userInfo);
        // 更新用户登录邮箱
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtils.getLoginUser().getId())
                .username(saveEmail.getEmail())
                .build();
        userAuthMapper.updateById(userAuth);
        return updateToken(userAuth.getUsername());
    }

    /**
     * @param userInfo
     * @description: 更新用户信息
     * @auther apecode
     * @date 2022/6/15 16:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVo userInfo) {
        UserInfo info = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .nickname(userInfo.getNickname())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .isEmailNotice(userInfo.getIsEmailNotice())
                .build();
        userInfoMapper.updateById(info);
        // 删除redis用户缓存，让其重新添加
        redisService.hDel(USER_CACHE, UserUtils.getLoginUser().getUsername());
    }

    /**
     * @return {@link UserInfoFrontDto}
     * @description: 获取用户信息
     * @auther apecode
     * @date 2022/6/15 20:46
     */
    @Override
    public UserInfoFrontDto getUserInfo() {
        UserInfo userInfo = userInfoMapper.selectById(UserUtils.getLoginUser().getUserInfoId());
        UserInfoFrontDto userInfoFrontDto = BeanCopyUtils.copyObject(userInfo, UserInfoFrontDto.class);
        userInfoFrontDto.setLoginType(UserUtils.getLoginUser().getLoginType());
        return userInfoFrontDto;
    }

    /**
     * @param unbindEmailVo
     * @description: 解绑用户邮箱
     * @auther apecode
     * @date 2023/1/6 1:31
     */
    @Override
    public String unbindEmail(SaveOrUnbindEmailVo unbindEmailVo) {
        checkCodeAndCaptcha(unbindEmailVo);
        UserAuth userAuth = userAuthMapper.selectById(UserUtils.getLoginUser().getId());
        UserInfo userInfo = userInfoMapper.selectById(userAuth.getUserInfoId());
        if (!userInfo.getEmail().equals(unbindEmailVo.getEmail())) throw new BizException("邮箱与账号不匹配");
        if (!userInfo.getBindQQ()) throw new BizException("请先绑定其他登录方式");
        LoginQq loginQq = loginQqMapper.selectOne(Wrappers.<LoginQq>lambdaQuery().eq(LoginQq::getUserAuthId, userAuth.getId()));
        userAuthMapper.updateById(UserAuth.builder().username(loginQq.getOpenId()).id(UserUtils.getLoginUser().getId()).build());
        userInfoMapper.update(UserInfo.builder().build(), Wrappers.<UserInfo>lambdaUpdate().eq(UserInfo::getId, userInfo.getId()).set(UserInfo::getEmail, null).set(UserInfo::getIsEmailNotice, false));
        return updateToken(loginQq.getOpenId());
    }

    /**
     * @param qqLoginVo
     * @description: 绑定QQ
     * @auther apecode
     * @date 2023/1/6 11:15
     */
    @Override
    public void bindQQ(QQLoginVo qqLoginVo) {
        qqTokenUtils.checkQQToken(qqLoginVo);
        LoginQq existsBindQQ = loginQqMapper.selectOne(Wrappers.<LoginQq>lambdaQuery().eq(LoginQq::getOpenId, qqLoginVo.getOpenId()));
        if (Objects.nonNull(existsBindQQ)) throw new BizException("QQ已被绑定");
        LoginQq loginQq = LoginQq.builder()
                .openId(qqLoginVo.getOpenId())
                .userAuthId(UserUtils.getLoginUser().getId())
                .build();
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .bindQQ(true)
                .email(UserUtils.getLoginUser().getEmail())
                .build();
        loginQqMapper.insert(loginQq);
        userInfoMapper.updateById(userInfo);
    }

    /**
     * @description: 解绑QQ
     * @auther apecode
     * @date 2023/1/6 11:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unbindQQ() {
        UserInfo userInfo = userInfoMapper.selectById(UserUtils.getLoginUser().getUserInfoId());
        // 未绑定邮箱不给解绑QQ，否则拿什么登录？
        if (Objects.nonNull(userInfo) && StringUtils.isBlank(userInfo.getEmail())) {
            throw new BizException("请先绑定邮箱");
        }
        userInfo.setBindQQ(false);
        loginQqMapper.delete(Wrappers.<LoginQq>lambdaQuery().eq(LoginQq::getUserAuthId, UserUtils.getLoginUser().getId()));
        userInfoMapper.updateById(userInfo);
    }

    /**
     * @return {@link String}
     * @description: 更新Jwt Token
     * @auther apecode
     * @date 2023/1/6 2:05
     */
    private String updateToken(String username) {
        // 删除redis用户缓存，让其登录时重新添加
        redisService.hDel(USER_CACHE, UserUtils.getLoginUser().getUsername());
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .username(username)
                .build();
        // 生成新的Token
        return jwtUtils.generateToken(userDetailsDto);
    }

    /**
     * @param unbindEmailVo
     * @description: 检查邮箱验证码和滑动验证码
     * @auther apecode
     * @date 2023/1/6 1:40
     */
    private void checkCodeAndCaptcha(SaveOrUnbindEmailVo emailVo) {
        // 滑动验证码
        anjiCaptchaUtils.checkCaptcha(emailVo.getCaptchaVerification());
        // 判断验证码是否正确
        if (StringUtils.isNotBlank(emailVo.getEmail()) && !emailVo.getCode().trim().equalsIgnoreCase((String) redisService.get(USER_CODE_KEY + emailVo.getEmail()))) {
            throw new BizException("验证码错误");
        }
        // 删除验证码
        redisService.del(USER_CODE_KEY + emailVo.getEmail());
    }

}
