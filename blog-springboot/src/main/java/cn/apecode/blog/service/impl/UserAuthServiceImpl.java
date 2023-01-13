package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.*;
import cn.apecode.blog.entity.UserAuth;
import cn.apecode.blog.entity.UserInfo;
import cn.apecode.blog.entity.UserRole;
import cn.apecode.blog.enums.EmailTypeEnum;
import cn.apecode.blog.enums.LoginTypeEnum;
import cn.apecode.blog.enums.RoleEnum;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.UserAuthMapper;
import cn.apecode.blog.mapper.UserInfoMapper;
import cn.apecode.blog.mapper.UserRoleMapper;
import cn.apecode.blog.service.RedisService;
import cn.apecode.blog.service.UserAuthService;
import cn.apecode.blog.service.WebsiteService;
import cn.apecode.blog.strategy.context.SocialLoginStrategyContext;
import cn.apecode.blog.utils.*;
import cn.apecode.blog.vo.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.apecode.blog.constant.RedisPrefixConst.USER_CACHE;
import static cn.apecode.blog.constant.RedisPrefixConst.USER_CODE_KEY;
import static cn.apecode.blog.constant.RedisPrefixConst.CODE_EXPIRE_TIME;
import static cn.apecode.blog.constant.CommonConst.DEFAULT_NICKNAME;
import static cn.apecode.blog.constant.CommonConst.DEFAULT_AVATAR;
import static cn.apecode.blog.constant.RabbitMQPrefixConst.EMAIL_EXCHANGE_NAME;
import static cn.apecode.blog.constant.RabbitMQPrefixConst.EMAIL_ROUTING_KEY_NAME;
import static cn.apecode.blog.enums.ZoneEnum.SHANGHAI;
import static cn.apecode.blog.enums.LoginTypeEnum.EMAIL;

/**
 * <p>
 * 用户账号 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AnjiCaptchaUtils anjiCaptchaUtils;
    @Autowired
    private SocialLoginStrategyContext socialLoginStrategyContext;

    /**
     * @param conditionVo
     * @return {@link PageResult<UserInfoBackDto>}
     * @description: 获取后台用户列表
     * @auther apecode
     * @date 2022/6/9 11:55
     */
    @Override
    public PageResult<UserInfoBackDto> listUserInfoBack(ConditionVo conditionVo) {
        // 获取后台用户数量
        Integer count = userAuthMapper.userCount(conditionVo);
        // 获取后台用户列表
        List<UserInfoBackDto> userInfoBackDtoList = userAuthMapper.listUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        userInfoBackDtoList.stream().peek(userInfo -> {
            userInfo.setUserAuthId(SecurityUtils.encrypt(userInfo.getUserAuthId()));
            userInfo.setUserInfoId(SecurityUtils.encrypt(userInfo.getUserInfoId()));
            userInfo.getRoleList().stream().peek((role) -> role.setId(SecurityUtils.encrypt(role.getId()))).collect(Collectors.toList());
        }).collect(Collectors.toList());
        return new PageResult<>(userInfoBackDtoList, count);
    }

    /**
     * @param password
     * @description: 修改密码
     * @auther apecode
     * @date 2022/6/9 15:36
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(PasswordVo password) {
        anjiCaptchaUtils.checkCaptcha(password.getCaptchaVerification());
        if (StringUtils.isNotBlank(password.getCode()) && !password.getCode().trim().equalsIgnoreCase((String) redisService.get(USER_CODE_KEY + UserUtils.getLoginUser().getUsername()))) {
            throw new BizException("验证码错误");
        }
        if (Objects.isNull(UserUtils.getLoginUser().getEmail())) throw new BizException("请先绑定邮箱");
        UserAuth userAuth = userAuthMapper.selectOne(Wrappers.<UserAuth>lambdaQuery()
                .eq(UserAuth::getId, UserUtils.getLoginUser().getId()));
        if (Objects.nonNull(userAuth) && BCrypt.checkpw(password.getOldPassword(), userAuth.getPassword())) {
            UserAuth auth = UserAuth.builder()
                    .id(UserUtils.getLoginUser().getId())
                    .password(BCrypt.hashpw(password.getNewPassword(), BCrypt.gensalt()))
                    .build();
            // 更新数据库
            userAuthMapper.updateById(auth);
            // 删除用户缓存
            redisService.hDel(USER_CACHE, userAuth.getUsername());
            // 修改成功删除code
            redisService.del(USER_CODE_KEY + userAuth.getUsername());
        } else {
            throw new BizException("旧密码不正确");
        }
    }

    /**
     * @param findPassword
     * @description: 找回密码
     * @auther apecode
     * @date 2022/6/9 21:28
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void findPassword(@Validated @RequestBody FindPasswordVo findPassword) {
        anjiCaptchaUtils.checkCaptcha(findPassword.getCaptchaVerification());
        if (!checkUser(findPassword.getEmail(), findPassword.getCode())) {
            throw new BizException("邮箱尚未注册");
        }
        // 生成新密码并写入数据库
        String password = BCrypt.hashpw(findPassword.getNewPassword(), BCrypt.gensalt());
        userAuthMapper.update(new UserAuth(), Wrappers.<UserAuth>lambdaUpdate()
                .set(UserAuth::getPassword, password)
                .eq(UserAuth::getUsername, findPassword.getEmail()));
        // 删除用户缓存
        redisService.hDel(USER_CACHE, findPassword.getEmail());
        // 删除code
        redisService.del(USER_CODE_KEY + findPassword.getEmail());
    }


    /**
     * @param register
     * @description: 用户注册
     * @auther apecode
     * @date 2022/6/9 23:13
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVo register) {
        anjiCaptchaUtils.checkCaptcha(register.getCaptchaVerification());
        if (checkUser(register.getEmail(), register.getCode())) throw new BizException("该邮箱已被注册");
        String ipAddress = IpUtils.getIpAddress(request);
        IpSourceDto ipSourceFromAmap = IpUtils.getIpSourceFromAmap(ipAddress);
        String source;
        String location;
        if (Objects.nonNull(ipSourceFromAmap)) {
            source = IpUtils.cutProvince(ipSourceFromAmap);
            location = ipSourceFromAmap.getLocation();
        } else {
            source = IpUtils.getIpSource(ipAddress);
            location = null;
        }
        UserAgent userAgent = IpUtils.getUserAgent(request);
        // 新增用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(DEFAULT_NICKNAME + RandomUtils.getUUID(10))
                .uid(RandomUtils.getUid())
                .avatar(StringUtils.isNotBlank(websiteService.getWebsiteConfigure().getDefaultAvatar()) ? websiteService.getWebsiteConfigure().getDefaultAvatar() : DEFAULT_AVATAR)
                .email(register.getEmail())
                .isEmailNotice(false)
                .bindQQ(false)
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        userInfoMapper.insert(userInfo);
        // 新增账号信息
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(register.getEmail())
                .password(BCrypt.hashpw(register.getPassword(), BCrypt.gensalt()))
                .updateTime(CommonUtils.getLocalDateTime())
                .loginType(EMAIL.getType())
                .ipAddress(ipAddress)
                .ipSource(source)
                .rectangle(location)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())))
                .build();
        userAuthMapper.insert(userAuth);
        UserRole userRole = UserRole.builder()
                .userId(userAuth.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        userRoleMapper.insert(userRole);
        // 注册成功删除code
        redisService.del(USER_CODE_KEY + register.getEmail());
    }

    /**
     * @param email
     * @description: 发送邮箱验证码
     * @auther apecode
     * @date 2022/6/10 20:19
     */
    @Override
    public void sendCode(String email) {
        if (redisService.getExpire(USER_CODE_KEY + email) >= 540) {
            throw new BizException("请等待一分钟后重试!");
        }
        if (!CommonUtils.checkEmail(email)) {
            throw new BizException("请输入正确的邮箱");
        }
        // 生成6位验证码
        String code = RandomUtils.getUUID(6).toUpperCase();
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .subject("您有新的验证码，请查收")
                .text("您的验证码为：" + code + " 十分钟内有效，请务必不要泄露")
                .type(EmailTypeEnum.CODE.getType())
                .isHtml(false)
                .build();
        String msgId = RandomUtils.getUUID(15);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(msgId);
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE_NAME, EMAIL_ROUTING_KEY_NAME, new Message(JSON.toJSONBytes(emailDto), messageProperties), new CorrelationData(msgId));
        redisService.set(USER_CODE_KEY + email, code, CODE_EXPIRE_TIME);
    }

    /**
     * @param userEnable
     * @description: 修改用户禁用状态
     * @auther apecode
     * @date 2022/6/14 21:23
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserEnable(UserEnableVo userEnable) {
        Integer userId = SecurityUtils.decrypt(userEnable.getId());
        if (Objects.isNull(userId)) throw new BizException("用户id有误");
        if (userId.equals(UserUtils.getLoginUser().getId())) {
            throw new BizException("无法修改自己的状态");
        }
        UserAuth userAuth = userAuthMapper.selectOne(Wrappers.<UserAuth>lambdaQuery()
                .select(UserAuth::getId, UserAuth::getUsername, UserAuth::getEnable)
                .eq(UserAuth::getId, userId));
        if (Objects.isNull(userAuth)) {
            throw new BizException("未找到用户");
        }
        userAuth.setEnable(userEnable.getEnable());
        userAuthMapper.updateById(userAuth);
        redisService.hDel(USER_CACHE, userAuth.getUsername());
    }

    /**
     * @param qqLoginVo
     * @return {@link UserInfoDto}
     * @description: 第三方QQ登录
     * @auther apecode
     * @date 2023/1/5 12:31
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDto qqLogin(QQLoginVo qqLoginVo) {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLoginVo), LoginTypeEnum.QQ);
    }

    /**
     * @return {@link boolean}
     * @description: 校验用户数据是否合法
     * @auther apecode
     * @date 2022/6/9 23:15
     */
    private boolean checkUser(String email, String code) {
        if (StringUtils.isNotBlank(code) && !code.trim().equalsIgnoreCase((String) redisService.get(USER_CODE_KEY + email))) {
            throw new BizException("验证码错误");
        }
        // 查询邮箱是否存在
        return userAuthMapper.exists(Wrappers.<UserAuth>lambdaQuery().eq(UserAuth::getUsername, email));
    }
}