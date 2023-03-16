package cn.apecode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description: 后台分类
 * @author: apecode
 * @date: 2022-07-04 18:54
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台分类", description = "后台分类展示")
public class CategoryBackDto {

    @ApiModelProperty(value = "分类id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "分类名称", name = "name", dataType = "String")
    private String name;

    @ApiModelProperty(value = "文章数量", name = "articleCount", dataType = "Integer")
    private Integer articleCount;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
