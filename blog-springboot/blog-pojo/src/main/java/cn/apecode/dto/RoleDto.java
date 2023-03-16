package cn.apecode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 后台角色
 * @author: apecode
 * @date: 2022-06-16 19:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台角色", description = "返回后台角色信息")
public class RoleDto {

    @ApiModelProperty(value = "角色id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "角色权限名", name = "roleAuth", dataType = "String")
    private String roleAuth;

    @ApiModelProperty(value = "角色描述", name = "roleDesc", dataType = "String")
    private String roleDesc;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否启用（1是/0否）", name = "enable", dataType = "Boolean")
    private Boolean isEnable;

    @ApiModelProperty(value = "资源id列表", name = "resourceIdList", dataType = "List<String>")
    private List<String> resourceIdList;

    @ApiModelProperty(value = "菜单id列表", name = "menuIdList", dataType = "List<String>")
    private List<String> menuIdList;

}
