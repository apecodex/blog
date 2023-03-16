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
 * @description: 资源
 * @author: apecode
 * @date: 2022-06-17 20:23
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "资源", description = "后台资源信息")
public class ResourceDto {

    @ApiModelProperty(value = "资源id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "资源名称", name = "name", dataType = "String")
    private String name;

    @ApiModelProperty(value = "资源路径", name = "url", dataType = "String")
    private String url;

    @ApiModelProperty(value = "请求方式", name = "requestMethod", dataType = "String")
    private String requestMethod;

    @ApiModelProperty(value = "资源父id", name = "parentId", dataType = "String")
    private String parentId;

    @ApiModelProperty(value = "是否启用（1是/0否）", name = "enable", dataType = "Boolean")
    private Boolean isEnable;

    @ApiModelProperty(value = "是否匿名访问（1是/0否）", name = "isAnonymous")
    private Boolean isAnonymous;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "子资源", name = "children", dataType = "List<ResourceDto>")
    private List<ResourceDto> children;

}
