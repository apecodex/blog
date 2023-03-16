package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description: 分类
 * @author: apecode
 * @date: 2022-06-26 16:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类", description = "分类")
public class CategoryVo {

    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "String")
    private String categoryId;

    @NotBlank(message = "分类名不能为空")
    @ApiModelProperty(name = "name", value = "分类名", dataType = "String", required = true)
    private String name;

}
