package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description: 置顶
 * @author: apecode
 * @date: 2022-06-28 15:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "置顶", description = "置顶")
public class TopVo {

    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "id", value = "id", dataType = "String", required = true)
    private String id;

    @NotNull(message = "置顶状态不能为空")
    @ApiModelProperty(name = "isTop", value = "置顶状态", dataType = "Boolean", required = true)
    private Boolean isTop;

}
