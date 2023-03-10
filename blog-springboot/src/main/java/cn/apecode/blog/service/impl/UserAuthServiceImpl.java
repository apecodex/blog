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
import lombok.RequiredArgsConstructor;
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
 * ???????????? ???????????????
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    private final UserAuthMapper userAuthMapper;
    private final UserInfoMapper userInfoMapper;
    private final UserRoleMapper userRoleMapper;
    private final RedisService redisService;
    private final WebsiteService websiteService;
    private final RabbitTemplate rabbitTemplate;
    private final HttpServletRequest request;
    private final AnjiCaptchaUtils anjiCaptchaUtils;
    private final SocialLoginStrategyContext socialLoginStrategyContext;

    /**
     * @param conditionVo
     * @return {@link PageResult<UserInfoBackDto>}
     * @description: ????????????????????????
     * @auther apecode
     * @date 2022/6/9 11:55
     */
    @Override
    public PageResult<UserInfoBackDto> listUserInfoBack(ConditionVo conditionVo) {
        // ????????????????????????
        Integer count = userAuthMapper.userCount(conditionVo);
        // ????????????????????????
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
     * @description: ????????????
     * @auther apecode
     * @date 2022/6/9 15:36
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(PasswordVo password) {
        anjiCaptchaUtils.checkCaptcha(password.getCaptchaVerification());
        if (StringUtils.isNotBlank(password.getCode()) && !password.getCode().trim().equalsIgnoreCase((String) redisService.get(USER_CODE_KEY + UserUtils.getLoginUser().getUsername()))) {
            throw new BizException("???????????????");
        }
        if (Objects.isNull(UserUtils.getLoginUser().getEmail())) throw new BizException("??????????????????");
        UserAuth userAuth = userAuthMapper.selectOne(Wrappers.<UserAuth>lambdaQuery()
                .eq(UserAuth::getId, UserUtils.getLoginUser().getId()));
        if (Objects.nonNull(userAuth) && BCrypt.checkpw(password.getOldPassword(), userAuth.getPassword())) {
            UserAuth auth = UserAuth.builder()
                    .id(UserUtils.getLoginUser().getId())
                    .password(BCrypt.hashpw(password.getNewPassword(), BCrypt.gensalt()))
                    .build();
            // ???????????????
            userAuthMapper.updateById(auth);
            // ??????????????????
            redisService.hDel(USER_CACHE, userAuth.getUsername());
            // ??????????????????code
            redisService.del(USER_CODE_KEY + userAuth.getUsername());
        } else {
            throw new BizException("??????????????????");
        }
    }

    /**
     * @param findPassword
     * @description: ????????????
     * @auther apecode
     * @date 2022/6/9 21:28
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void findPassword(@Validated @RequestBody FindPasswordVo findPassword) {
        anjiCaptchaUtils.checkCaptcha(findPassword.getCaptchaVerification());
        if (!checkUser(findPassword.getEmail(), findPassword.getCode())) {
            throw new BizException("??????????????????");
        }
        // ?????????????????????????????????
        String password = BCrypt.hashpw(findPassword.getNewPassword(), BCrypt.gensalt());
        userAuthMapper.update(new UserAuth(), Wrappers.<UserAuth>lambdaUpdate()
                .set(UserAuth::getPassword, password)
                .eq(UserAuth::getUsername, findPassword.getEmail()));
        // ??????????????????
        redisService.hDel(USER_CACHE, findPassword.getEmail());
        // ??????code
        redisService.del(USER_CODE_KEY + findPassword.getEmail());
    }


    /**
     * @param register
     * @description: ????????????
     * @auther apecode
     * @date 2022/6/9 23:13
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVo register) {
        anjiCaptchaUtils.checkCaptcha(register.getCaptchaVerification());
        if (checkUser(register.getEmail(), register.getCode())) throw new BizException("?????????????????????");
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
        // ??????????????????
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
        // ??????????????????
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
        // ??????????????????code
        redisService.del(USER_CODE_KEY + register.getEmail());
    }

    /**
     * @param email
     * @description: ?????????????????????
     * @auther apecode
     * @date 2022/6/10 20:19
     */
    @Override
    public void sendCode(String email) {
        if (redisService.getExpire(USER_CODE_KEY + email) >= 540) {
            throw new BizException("???????????????????????????!");
        }
        if (!CommonUtils.checkEmail(email)) {
            throw new BizException("????????????????????????");
        }
        // ??????6????????????
        String code = RandomUtils.getUUID(6).toUpperCase();
        EmailDto emailDto = EmailDto.builder()
                .email(email)
                .subject("?????????????????????????????????")
                .text("?????????????????????" + code + " ??????????????????????????????????????????")
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
     * @description: ????????????????????????
     * @auther apecode
     * @date 2022/6/14 21:23
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserEnable(UserEnableVo userEnable) {
        Integer userId = SecurityUtils.decrypt(userEnable.getId());
        if (Objects.isNull(userId)) throw new BizException("??????id??????");
        if (userId.equals(UserUtils.getLoginUser().getId())) {
            throw new BizException("???????????????????????????");
        }
        UserAuth userAuth = userAuthMapper.selectOne(Wrappers.<UserAuth>lambdaQuery()
                .select(UserAuth::getId, UserAuth::getUsername, UserAuth::getEnable)
                .eq(UserAuth::getId, userId));
        if (Objects.isNull(userAuth)) {
            throw new BizException("???????????????");
        }
        userAuth.setEnable(userEnable.getEnable());
        userAuthMapper.updateById(userAuth);
        redisService.hDel(USER_CACHE, userAuth.getUsername());
    }

    /**
     * @param qqLoginVo
     * @return {@link UserInfoDto}
     * @description: ?????????QQ??????
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
     * @description: ??????????????????????????????
     * @auther apecode
     * @date 2022/6/9 23:15
     */
    private boolean checkUser(String email, String code) {
        if (StringUtils.isNotBlank(code) && !code.trim().equalsIgnoreCase((String) redisService.get(USER_CODE_KEY + email))) {
            throw new BizException("???????????????");
        }
        // ????????????????????????
        return userAuthMapper.exists(Wrappers.<UserAuth>lambdaQuery().eq(UserAuth::getUsername, email));
    }
}
