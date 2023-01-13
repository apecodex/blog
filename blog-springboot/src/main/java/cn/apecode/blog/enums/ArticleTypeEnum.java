package cn.apecode.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 文章类型枚举
 * @author: apecode
 * @date: 2022-06-28 17:31
 **/
@Getter
@AllArgsConstructor
public enum ArticleTypeEnum {

    ORIGINAL(1, "原创"),

    REPRINT(2, "转载"),

    TRANSLATE(3, "翻译");

    private final Integer type;
    private final String desc;

    public static ArticleTypeEnum getArticleTypeEnum(Integer type) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
