package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 逻辑删除
 * @author: apecode
 * @date: 2022-06-24 00:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "逻辑删除", description = "逻辑删除")
public class DeleteVo {

    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<String>")
    private List<String> idList;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(name = "isDelete", value = "状态", required = true, dataType = "Boolean")
    private Boolean isDelete;

}
