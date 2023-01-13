package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @description: 用户角色
 * @author: apecode
 * @date: 2022-06-09 11:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@ApiModel(value = "用户角色", description = "返回用户角色信息")
public class UserRoleDto {

    @ApiModelProperty(value = "角色id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "角色描述", dataType = "String")
    private String roleDesc;
}
