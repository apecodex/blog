package cn.apecode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 评论类型枚举
 * @author: apecode
 * @date: 2022-07-07 21:31
 **/
@Getter
@AllArgsConstructor
public enum CommentTypeEnum {

    ARTICLE(1, "文章", "/article/"),
    TALK(2, "说说", "/talk/");

    // 类型
    private final Integer type;
    // 描述
    private final String desc;
    // 路径
    private final String path;

    public static CommentTypeEnum getCommentEnum(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
