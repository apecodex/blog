package cn.apecode.blog.strategy.impl;

import cn.apecode.blog.dto.*;
import cn.apecode.blog.entity.*;
import cn.apecode.blog.enums.LoginTypeEnum;
import cn.apecode.blog.enums.RoleEnum;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.*;
import cn.apecode.blog.service.RedisService;
import cn.apecode.blog.service.impl.UserDetailsServicesImpl;
import cn.apecode.blog.strategy.SocialLoginStrategy;
import cn.apecode.blog.utils.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static cn.apecode.blog.constant.RedisPrefixConst.USER_CACHE;


/**
 * @description: 第三方登录抽象类
 * @author: apecode
 * @date: 2023-01-04 22:16
 **/
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {

    @Autowired
    private LoginQqMapper loginQqMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private UserDetailsServicesImpl userDetailsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Override
    public UserInfoDto login(String data) {
        UserDetailsDto userDetailsDto = null;
        // 获取第三方用户信息
        SocialTokenDto socialToken = getSocialToken(data);
        // 判断是否已经注册
        UserAuth userAuth = getUserAuth(socialToken);
        if (Objects.nonNull(userAuth)) {
            // 返回数据库用户信息
            userDetailsDto = getUserDetails(userAuth);
        } else {
            // 获取第三方用户信息，保存至数据库并返回
            userDetailsDto = saveUserDetails(socialToken);
        }
        // 判断账号是否禁用
        if (!userDetailsDto.isEnabled()) {
            throw new BizException("账号被禁用");
        }
        // 生成Token
        String token = jwtUtils.generateToken(userDetailsDto);
        userDetailsDto.setToken(token);
        userDetailsDto.setTokenHead(tokenHead);
        userDetailsDto.setPassword(null);
        // 安全上下文，将信息交给spring security管理
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetailsDto, null, userDetailsDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 更新登录信息
        updateUserLoginInfo(userDetailsDto);
        return BeanCopyUtils.copyObject(userDetailsDto, UserInfoDto.class);
    }

    /**
     * 获取第三方token信息
     * @param data
     * @return {@link cn.apecode.blog.dto.SocialTokenDto}
     */
    public abstract SocialTokenDto getSocialToken(String data);

    /**
     * 获取第三方用户信息
     * @param socialToken
     * @return {@link cn.apecode.blog.dto.SocialUserInfoDto}
     */
    public abstract SocialUserInfoDto getSocialUserInfo(SocialTokenDto socialToken);

    /**
     * 获取用户账号
     * @param socialToken
     * @return {@link cn.apecode.blog.entity.UserAuth}
     */
    private UserAuth getUserAuth(SocialTokenDto socialToken) {
        UserAuth userAuth = null;
        LoginQq loginQq = loginQqMapper.selectOne(Wrappers.<LoginQq>lambdaQuery().eq(LoginQq::getOpenId, socialToken.getOpenId()));
        if (Objects.nonNull(loginQq)) {
            userAuth = userAuthMapper.selectOne(Wrappers.<UserAuth>lambdaQuery().eq(UserAuth::getId, loginQq.getUserAuthId()));
        }
        return userAuth;
    }

    /**
     * @description: 获取用户信息
     * @param userAuth
     * @return {@link UserDetailsDto}
     * @auther apecode
     * @date 2023/1/5 12:30
    */
    private UserDetailsDto getUserDetails(UserAuth userAuth) {
        // 删除Redis用户信息，以便下次获取数据时准确
        redisService.hDel(USER_CACHE, userAuth.getUsername());
        userAuth.setLoginType(LoginTypeEnum.QQ.getType());
        // 封装用户信息
        return userDetailsService.convertUserDetail(userAuth, request);
    }

    /**
     * @description: 新增用户信息
     * @param socialToken
     * @return {@link UserDetailsDto}
     * @auther apecode
     * @date 2023/1/5 12:30
    */
    private UserDetailsDto saveUserDetails(SocialTokenDto socialToken) {
        // 获取ip信息
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
        // 获取第三方用户信息
        SocialUserInfoDto socialUserInfo = getSocialUserInfo(socialToken);
        // 保存用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .uid(RandomUtils.getUid())
                .bindQQ(true)
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        userInfoMapper.insert(userInfo);
        // 保存账号信息
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(socialToken.getOpenId())
                .password(BCrypt.hashpw(socialToken.getAccessToken(), BCrypt.gensalt()))
                .loginType(socialToken.getLoginType())
                .lastLoginTime(CommonUtils.getLocalDateTime())
                .ipAddress(ipAddress)
                .ipSource(source)
                .rectangle(location)
                .enable(true)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        userAuthMapper.insert(userAuth);
        // 绑定角色
        UserRole userRole = UserRole.builder()
                .userId(userAuth.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        userRoleMapper.insert(userRole);
        // 保存openId并绑定账号
        LoginQq loginQq = LoginQq.builder()
                .userAuthId(userAuth.getId())
                .openId(socialToken.getOpenId())
                .build();
        loginQqMapper.insert(loginQq);
        return userDetailsService.convertUserDetail(userAuth, request);
    }


    /**
     * @description: 更新登录信息
     * @param userDetailsDto
     * @auther apecode
     * @date 2023/1/5 12:30
    */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void updateUserLoginInfo(UserDetailsDto userDetailsDto) {
        UserAuth userAuth = UserAuth.builder()
                .id(userDetailsDto.getId())
                .loginType(LoginTypeEnum.QQ.getType())
                .ipAddress(userDetailsDto.getIpAddress())
                .ipSource(userDetailsDto.getIpSource())
                .browser(userDetailsDto.getBrowser())
                .os(userDetailsDto.getOs())
                .lastLoginTime(userDetailsDto.getLastLoginTime())
                .build();
        // 保存登录记录
        LoginLog loginLog = LoginLog.builder()
                .userId(userDetailsDto.getUserInfoId())
                .loginType(LoginTypeEnum.QQ.getType())
                .ipAddress(userDetailsDto.getIpAddress())
                .ipSource(userDetailsDto.getIpSource())
                .browser(userDetailsDto.getBrowser())
                .os(userDetailsDto.getOs())
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        IpSourceDto ipSourceFromAmap = IpUtils.getIpSourceFromAmap(userDetailsDto.getIpAddress());
        if (Objects.nonNull(ipSourceFromAmap)) {
            String source = IpUtils.cutProvince(ipSourceFromAmap);
            userAuth.setIpSource(source);
            loginLog.setIpSource(source);
            if (!ipSourceFromAmap.getLocation().split(",")[0].equals("null")) {
                String location = ipSourceFromAmap.getLocation();
                userAuth.setRectangle(location);
                loginLog.setRectangle(location);
            }
        }
        userAuthMapper.updateById(userAuth);
        loginLogMapper.insert(loginLog);
    }
}
