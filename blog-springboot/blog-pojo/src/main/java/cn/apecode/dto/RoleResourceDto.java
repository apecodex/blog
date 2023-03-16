package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: apecode
 * @date: 2022-05-28 19:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "角色资源", description = "角色对应的资源")
public class RoleResourceDto {

    @ApiModelProperty(value = "资源id", dataType = "String")
    private Integer id;

    @ApiModelProperty(value = "路径", dataType = "String")
    private String url;

    @ApiModelProperty(value = "请求方式", dataType = "String")
    private String requestMethod;

    @ApiModelProperty(value = "角色名", dataType = "List<String>")
    private List<String> roleList;
}
