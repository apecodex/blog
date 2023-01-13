package cn.apecode.blog.strategy.impl;

import cn.apecode.blog.config.QQConfigYml;
import cn.apecode.blog.dto.QQUserInfoDto;
import cn.apecode.blog.dto.SocialTokenDto;
import cn.apecode.blog.dto.SocialUserInfoDto;
import cn.apecode.blog.enums.LoginTypeEnum;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.utils.QQTokenUtils;
import cn.apecode.blog.vo.QQLoginVo;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.apecode.blog.enums.StatusCodeEnum.QQ_LOGIN_ERROR;
import static cn.apecode.blog.constant.SocialLoginConst.QQ_OPEN_ID;
import static cn.apecode.blog.constant.SocialLoginConst.ACCESS_TOKEN;
import static cn.apecode.blog.constant.SocialLoginConst.OAUTH_CONSUMER_KEY;

/**
 * @description: qq登录策略实现
 * @author: apecode
 * @date: 2023-01-05 12:09
 **/
@Service("qqLoginStrategyImpl")
public class QQLoginStrategyImpl extends AbstractSocialLoginStrategyImpl{

    @Autowired
    private QQConfigYml qqConfigYml;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private QQTokenUtils qqTokenUtils;
    @Override
    public SocialTokenDto getSocialToken(String data) {
        QQLoginVo qqLoginVo = JSON.parseObject(data, QQLoginVo.class);
        // 校验QQ token信息
        qqTokenUtils.checkQQToken(qqLoginVo);
        // 返回Token信息
        return SocialTokenDto.builder()
                .openId(qqLoginVo.getOpenId())
                .accessToken(qqLoginVo.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .build();
    }

    @Override
    public SocialUserInfoDto getSocialUserInfo(SocialTokenDto socialToken) {
        // 定义请求参数
        Map<String, String> formData = new HashMap<>(3);
        formData.put(QQ_OPEN_ID, socialToken.getOpenId());
        formData.put(ACCESS_TOKEN, socialToken.getAccessToken());
        formData.put(OAUTH_CONSUMER_KEY, qqConfigYml.getAppId());
        // 获取QQ返回的用户信息
        QQUserInfoDto qqUserInfoDto = JSON.parseObject(restTemplate.getForObject(qqConfigYml.getUserInfoUrl(), String.class, formData), QQUserInfoDto.class);
        if (Objects.isNull(qqUserInfoDto)) throw new BizException(QQ_LOGIN_ERROR);
        // 返回用户信息
        return SocialUserInfoDto.builder()
                .nickname(qqUserInfoDto.getNickname())
                .avatar(qqUserInfoDto.getFigureurl_qq_2())
                .build();
    }

}
