package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 标签选项
 * @author: apecode
 * @date: 2022-06-17 23:18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "标签选项", description = "返回前端下拉选项")
public class LabelOptionDto {

    @ApiModelProperty(value = "选项id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "选项名", name = "label", dataType = "String")
    private String label;

    @ApiModelProperty(value = "子选项", name = "children", dataType = "List<LabelOptionDto>")
    private List<LabelOptionDto> children;
}
