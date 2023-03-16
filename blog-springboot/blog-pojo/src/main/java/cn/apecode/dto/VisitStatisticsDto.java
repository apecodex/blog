package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 访问量统计
 * @author: apecode
 * @date: 2022-07-13 23:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户访问量统计", description = "用户访问量统计")
public class VisitStatisticsDto {

    @ApiModelProperty(value = "日期", name = "date", dataType = "String")
    private String date;

    @ApiModelProperty(value = "浏览量", name = "viewsCount", dataType = "Integer")
    private Integer viewsCount;

}
