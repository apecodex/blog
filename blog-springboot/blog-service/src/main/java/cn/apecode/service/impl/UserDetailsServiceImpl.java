package cn.apecode.service.impl;

import cn.apecode.common.utils.BeanCopyUtils;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.UserCacheDto;
import cn.apecode.common.dto.UserDetailsDto;
import cn.apecode.entity.UserAuth;
import cn.apecode.entity.UserInfo;
import cn.apecode.mapper.RoleMapper;
import cn.apecode.mapper.UserAuthMapper;
import cn.apecode.mapper.UserInfoMapper;
import cn.apecode.service.RedisService;
import cn.apecode.common.utils.IpUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.apecode.common.constant.RedisPrefixConst.*;

/**
 * @description: 用户详细信息
 * @author: apecode
 * @date: 2022-05-27 00:59
 **/
@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAuthMapper userAuthMapper;
    private final UserInfoMapper userInfoMapper;
    private final RoleMapper roleMapper;
    private final RedisService redisService;
    private final HttpServletRequest request;
    private UserCacheDto userCacheDto = null;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BadCredentialsException("账号不能为空");
        }
        // 获取用户缓存信息
        Object userCacheObj = redisService.hGet(USER_CACHE, username);
        if (Objects.nonNull(userCacheObj)) {
            userCacheDto = JSON.parseObject(userCacheObj.toString(), UserCacheDto.class);
        }
        UserAuth userAuth;
        if (Objects.isNull(userCacheDto)) {
            userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                    .eq(UserAuth::getUsername, username));
            if (Objects.isNull(userAuth)) {
                throw new BadCredentialsException("账号不存在");
            }
        } else {
            userAuth = UserAuth.builder()
                    .id(userCacheDto.getId())
                    .username(userCacheDto.getUsername())
                    .password(userCacheDto.getPassword())
                    .userInfoId(userCacheDto.getUserInfoId())
                    .loginType(userCacheDto.getLoginType())
                    .enable(userCacheDto.getEnable())
                    .build();
        }
        return convertUserDetail(userAuth, request);
    }

    /**
     * @param userAuth, request
     * @return {@link UserDetails}
     * @description: 封装用户登录信息
     * @auther apecode
     * @date 2022/5/27 17:51
     */
    public UserDetailsDto convertUserDetail(UserAuth userAuth, HttpServletRequest request) {
        UserInfo userInfo;
        List<String> roles;
        // 获取设备信息
        String ipAddress = IpUtils.getIpAddress(request);
        String source = IpUtils.getIpSource(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        if (Objects.nonNull(userCacheDto)) {
            userInfo = BeanCopyUtils.copyObject(userCacheDto, UserInfo.class);
            roles = userCacheDto.getRoleList();
        } else {
            // 查询账号信息
            userInfo = userInfoMapper.selectById(userAuth.getUserInfoId());
            // 查询账号角色
            roles = roleMapper.ListRoleByUserId(userAuth.getId());
            // 更新账号信息
            if (!redisService.hHasKey(USER_CACHE, userAuth.getUsername())) {
                // 将用户信息添加到缓存中
                userCacheDto = BeanCopyUtils.copyObject(userInfo, UserCacheDto.class);
                userCacheDto.setId(userAuth.getId())
                        .setUsername(userAuth.getUsername())
                        .setPassword(userAuth.getPassword())
                        .setUserInfoId(userAuth.getUserInfoId())
                        .setLoginType(userAuth.getLoginType())
                        .setEnable(userAuth.getEnable())
                        .setRoleList(roles);
                String userCacheJSON = JSON.toJSONString(userCacheDto);
                redisService.hSet(USER_CACHE, userAuth.getUsername(), userCacheJSON);
            }
        }
        // 点赞信息
        Set<Object> articleLikeSet = redisService.sMembers(ARTICLE_USER_LIKE + userInfo.getId()).stream().map((id) -> SecurityUtils.encrypt(String.valueOf(id))).collect(Collectors.toSet());
        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + userInfo.getId()).stream().map((id) -> SecurityUtils.encrypt(String.valueOf(id))).collect(Collectors.toSet());
        Set<Object> talkLikeSet = redisService.sMembers(TALK_USER_LIKE + userInfo.getId()).stream().map((id) -> SecurityUtils.encrypt(String.valueOf(id))).collect(Collectors.toSet());
        // 避免登录其他账号时获取到上一个登录用户信息!!!
        userCacheDto = null;
        return UserDetailsDto.builder()
                .id(userAuth.getId())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .userInfoId(userAuth.getUserInfoId())
                .loginType(userAuth.getLoginType())
                .ipAddress(ipAddress)
                .ipSource(source)
                .enable(userAuth.getEnable())
                .nickname(userInfo.getNickname())
                .uid(userInfo.getUid())
                .email(userInfo.getEmail())
                .avatar(userInfo.getAvatar())
                .roleList(roles)
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(CommonUtils.getLocalDateTime())
                .build();
    }
}
