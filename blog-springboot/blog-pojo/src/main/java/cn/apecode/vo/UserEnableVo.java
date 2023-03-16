package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description: 用户禁用状态
 * @author: apecode
 * @date: 2022-06-14 21:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户状态", description = "用户状态")
public class UserEnableVo {

    @ApiModelProperty(name = "id", value = "用户id", required = true, dataType = "String")
    @NotNull(message = "用户id不能为空")
    private String id;

    @ApiModelProperty(name = "enable", value = "是否开启", required = true, dataType = "Boolean")
    @NotNull(message = "是否启用?")
    private Boolean enable;
}
