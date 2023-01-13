package cn.apecode.blog.enums;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "类型", name = "type", dataType = "Integer")
    private final Integer type;
    @ApiModelProperty(value = "描述", name = "desc", dataType = "String")
    private final String desc;
    @ApiModelProperty(value = "路径", name = "path", dataType = "String")
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
