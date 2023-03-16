package cn.apecode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 文章状态枚举
 * @author: apecode
 * @date: 2022-06-28 17:26
 **/
@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {

    PUBLIC(1, "公开"),

    PRIVATE(2, "私密"),

    DRAFT(3, "草稿");

    private final Integer status;
    private final String desc;

}
