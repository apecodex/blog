package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 文章贡献统计
 * @author: apecode
 * @date: 2022-07-13 23:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "文章贡献统计", description = "文章贡献统计")
public class ArticleStatisticsDto {

    @ApiModelProperty(value = "日期", name = "date", dataType = "String")
    private String date;

    @ApiModelProperty(value = "数量", name = "count", dataType = "Integer")
    private Integer count;

}
