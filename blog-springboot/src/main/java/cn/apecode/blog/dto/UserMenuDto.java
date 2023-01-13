package cn.apecode.blog.dto;

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
 * @description: 用户菜单
 * @author: apecode
 * @date: 2022-06-06 19:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户菜单", description = "返回当前用户页面展示的菜单")
public class UserMenuDto {

    @ApiModelProperty(value = "菜单id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "菜单标识", name = "name", dataType = "String")
    private String name;

    @ApiModelProperty(value = "菜单名称", name = "title", dataType = "String")
    private String title;

    @ApiModelProperty(value = "菜单路径", name = "path", dataType = "String")
    private String path;

    @ApiModelProperty(value = "组件", name = "component", dataType = "String")
    private String component;

    @ApiModelProperty(value = "菜单图标", name = "icon", dataType = "String")
    private String icon;

    @ApiModelProperty(value = "父菜单id", name = "parentId", dataType = "String")
    private String parentId;

    @ApiModelProperty(value = "排序", name = "orderNum", dataType = "Integer")
    private Integer orderNum;

    @ApiModelProperty(value = "是否启用（1是/0否）", name = "isEnable", dataType = "Boolean")
    private Boolean isEnable;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "子菜单列表", name = "children", dataType = "List<UserMenuDto>")
    private List<UserMenuDto> children;
}
