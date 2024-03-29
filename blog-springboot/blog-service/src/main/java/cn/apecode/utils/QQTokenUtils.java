package cn.apecode.utils;

import cn.apecode.common.constant.SocialLoginConst;
import cn.apecode.common.exception.BizException;
import cn.apecode.dto.QQTokenDto;
import cn.apecode.vo.QQLoginVo;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static cn.apecode.common.enums.StatusCodeEnum.QQ_LOGIN_ERROR;

/**
 * @description: 校验QQ Token
 * @author: apecode
 * @date: 2023-01-05 15:35
 **/
@RequiredArgsConstructor
@Component
public class QQTokenUtils {

    private final RestTemplate restTemplate;
    private final QQConfigYml qqConfigYml;

    /**
     * @param qqLoginVo
     * @description: 校验qq Token信息
     * @auther apecode
     * @date 2023/1/5 12:33
     */
    public void checkQQToken(QQLoginVo qqLoginVo) {
        // 根据token获取qq openId信息
        Map<String, String> qqData = new HashMap<>(1);
        qqData.put(SocialLoginConst.ACCESS_TOKEN, qqLoginVo.getAccessToken());
        String result = restTemplate.getForObject(qqConfigYml.getCheckTokenUrl(), String.class, qqData);
        QQTokenDto qqTokenDto = JSON.parseObject(result, QQTokenDto.class);
        try {
            // 判断openId是否一致
            if (!qqLoginVo.getOpenId().equals(qqTokenDto.getOpenid())) {
                throw new BizException(QQ_LOGIN_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(QQ_LOGIN_ERROR);
        }
    }
}
