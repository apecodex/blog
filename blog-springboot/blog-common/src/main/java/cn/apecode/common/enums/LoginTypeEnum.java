package cn.apecode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 登录方式枚举
 * @author: apecode
 * @date: 2022-05-29 20:24
 **/
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 邮箱登录
     */
    EMAIL(0, "邮箱登录", ""),
    QQ(1, "QQ登录", "qqLoginStrategyImpl");

    // 登录方式
    private final Integer type;

    // 描述
    private final String desc;

    // 策略
    private final String strategy;


    public static LoginTypeEnum getLoginTypeEnum(Integer type) {
        for (LoginTypeEnum value : LoginTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
