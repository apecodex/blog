package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 用户角色
 * @author: apecode
 * @date: 2022-06-14 22:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户角色", description = "用户角色")
public class UserRoleVo {

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(name = "userId", value = "用户id", required = true, dataType = "String")
    private String userId;

    @NotNull(message = "用户角色不能为空")
    @ApiModelProperty(name = "roleList", value = "角色id集合", dataType = "List<String>")
    private List<String> roleList;

}
