package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description: 保存或更新角色
 * @author: apecode
 * @date: 2022-06-16 14:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "插入或更新角色", description = "插入或更新角色")
public class RoleVo {

    @ApiModelProperty(name = "roleId", value = "角色id", dataType = "String")
    private String roleId;

    @NotBlank(message = "角色权限名不能为空")
    @ApiModelProperty(name = "roleAuth", value = "角色权限名", dataType = "String")
    private String roleAuth;

    @NotBlank(message = "角色权限描述不能为空")
    @ApiModelProperty(name = "roleDesc", value = "角色权限描述", dataType = "String")
    private String roleDesc;

    @ApiModelProperty(name = "resourceIdList", value = "资源列表", dataType = "List<String>")
    private List<String> resourceIdList;

    @ApiModelProperty(name = "menuIdList", value = "菜单列表", dataType = "List<String>")
    private List<String> menuIdList;

    @ApiModelProperty(name = "isEnable", value = "是否开启", dataType = "Boolean")
    private Boolean isEnable;
}
