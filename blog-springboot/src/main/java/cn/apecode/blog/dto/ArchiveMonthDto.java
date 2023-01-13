package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 文章归档
 * @author: apecode
 * @date: 2022-11-10 15:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "文章按月归档", description = "文章按月归档")
public class ArchiveMonthDto {

    @ApiModelProperty(value = "月份", name = "month", dataType = "String")
    private String month;

    @ApiModelProperty(value = "文章列表", name = "articles", dataType = "List<ArchiveDto>")
    private List<ArchiveDto> articles;
}
