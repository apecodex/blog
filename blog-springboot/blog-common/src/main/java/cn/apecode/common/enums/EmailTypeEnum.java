package cn.apecode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 邮件类型枚举
 * @author: apecode
 * @date: 2022-07-13 17:46
 **/
@Getter
@AllArgsConstructor
public enum EmailTypeEnum {

    CODE(1, "验证码"),
    COMMENT(2, "评论"),
    NOTICE(3, "通知"),
    MESSAGE(4, "留言");

    private final Integer type;
    private final String desc;

    public static EmailTypeEnum getMailTypeEnum(Integer type) {
        for (EmailTypeEnum value : EmailTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
