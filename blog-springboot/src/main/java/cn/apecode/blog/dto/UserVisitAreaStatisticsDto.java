package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 用户访问地区统计
 * @author: apecode
 * @date: 2022-07-13 23:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户访问地区统计", description = "用户访问地区统计")
public class UserVisitAreaStatisticsDto {

    @ApiModelProperty(value = "区域", name = "area", dataType = "String")
    private String area;

    @ApiModelProperty(value = "数量", name = "count", dataType = "Integer")
    private Integer count;
}
