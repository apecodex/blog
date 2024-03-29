package cn.apecode.strategy.context;

import cn.apecode.common.enums.LoginTypeEnum;
import cn.apecode.dto.UserInfoDto;
import cn.apecode.strategy.SocialLoginStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 第三方登录策略上下文
 * @author: apecode
 * @date: 2023-01-05 13:49
 **/
@Service
public class SocialLoginStrategyContext {

    @Resource
    private Map<String, SocialLoginStrategy> socialLoginStrategyMap;

    /**
     * @description: 执行第三方登录策略
     * @param data
     * @param loginTypeEnum
     * @return {@link UserInfoDto}
     * @auther apecode
     * @date 2023/1/5 13:55
    */
    public UserInfoDto executeLoginStrategy(String data, LoginTypeEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }

}
