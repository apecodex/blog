package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 菜单
 * @author: apecode
 * @date: 2022-06-18 16:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "菜单",description = "修改菜单")
public class MenuVo {

    @ApiModelProperty(name = "id", value = "菜单id", dataType = "String")
    private String id;

    @NotBlank(message = "菜单标识不能为空")
    @ApiModelProperty(name = "name", value = "菜单标识", dataType = "String")
    private String name;

    @NotBlank(message = "菜单名不能为空")
    @ApiModelProperty(name = "title", value = "菜单名", dataType = "String")
    private String title;

    @NotBlank(message = "路径不能为空")
    @ApiModelProperty(name = "path", value = "路径", dataType = "String")
    private String path;

    @NotBlank(message = "组件不能为空")
    @ApiModelProperty(name = "component", value = "组件名", dataType = "String")
    private String component;

    @NotBlank(message = "菜单icon不能为空")
    @ApiModelProperty(name = "icon", value = "图标", dataType = "String")
    private String icon;

    @ApiModelProperty(name = "parentId", value = "父菜单id", dataType = "String")
    private String parentId;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty(name = "orderNum", value = "排序", dataType = "Integer")
    private Integer orderNum;

    @ApiModelProperty(name = "isEnable", value = "是否启用", dataType = "Boolean")
    private Boolean isEnable;
}
