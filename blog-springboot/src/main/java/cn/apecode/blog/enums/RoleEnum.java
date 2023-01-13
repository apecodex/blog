package cn.apecode.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 角色枚举
 * @author: apecode
 * @date: 2022-06-09 23:40
 **/
@Getter
@AllArgsConstructor
public enum RoleEnum {

    /**
     * 管理员
     */
    ADMIN(1, "管理员", "admin"),
    /**
     * 普通用户
     */
    USER(2, "用户", "user"),
    /**
     * 测试账号
     */
    TEST(3, "测试", "test");

    /**
     * 角色id
     */
    private final Integer roleId;

    /**
     * 描述
     */
    private final String roleDesc;

    /**
     * 权限标签
     */
    private final String roleAuth;
}
