package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description: 资源
 * @author: apecode
 * @date: 2022-06-17 22:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "新增或更新资源", description = "新增或更新资源")
public class ResourceVo {

    @ApiModelProperty(name = "id", value = "资源id", dataType = "String")
    private String id;

    @NotBlank(message = "资源名不能为空")
    @ApiModelProperty(name = "name", value = "资源名称", required = true, dataType = "String")
    private String name;

    @ApiModelProperty(name = "url", value = "请求路径", dataType = "String")
    private String url;

    @ApiModelProperty(name = "requestMethod", value = "请求方式", dataType = "String")
    private String requestMethod;

    @ApiModelProperty(name = "parentId", value = "父资源id", dataType = "String")
    private String parentId;

    @ApiModelProperty(name = "isAnonymous", value = "是否匿名访问", required = true, dataType = "Boolean")
    private Boolean isAnonymous;

    @ApiModelProperty(name = "isEnable", value = "是否开启", required = true, dataType = "Boolean")
    private Boolean isEnable;

}
