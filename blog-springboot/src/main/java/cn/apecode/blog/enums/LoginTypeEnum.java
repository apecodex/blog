package cn.apecode.blog.enums;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "登录方式", name = "type", dataType = "Integer")
    private final Integer type;

    @ApiModelProperty(value = "描述", name = "desc", dataType = "String")
    private final String desc;

    @ApiModelProperty(value = "策略", name = "strategy", dataType = "String")
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
