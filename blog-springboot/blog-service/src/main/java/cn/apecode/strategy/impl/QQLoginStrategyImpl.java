package cn.apecode.strategy.impl;

import cn.apecode.utils.QQTokenUtils;
import cn.apecode.common.enums.LoginTypeEnum;
import cn.apecode.common.exception.BizException;
import cn.apecode.utils.QQConfigYml;
import cn.apecode.dto.QQUserInfoDto;
import cn.apecode.dto.SocialTokenDto;
import cn.apecode.dto.SocialUserInfoDto;
import cn.apecode.vo.QQLoginVo;
import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.apecode.common.constant.SocialLoginConst.*;
import static cn.apecode.common.enums.StatusCodeEnum.QQ_LOGIN_ERROR;

/**
 * @description: qq登录策略实现
 * @author: apecode
 * @date: 2023-01-05 12:09
 **/
@Service("qqLoginStrategyImpl")
public class QQLoginStrategyImpl extends AbstractSocialLoginStrategyImpl{

    @Resource
    private QQConfigYml qqConfigYml;
    @Resource
    private RestTemplate restTemplate;
    @Resource
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
