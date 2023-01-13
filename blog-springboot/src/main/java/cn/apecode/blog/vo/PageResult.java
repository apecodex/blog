package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 分页对象
 * @author: apecode
 * @date: 2022-06-09 09:30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分页Vo", description = "分页对象")
public class PageResult<T> {

    @ApiModelProperty(name = "recordList", value = "分页列表", dataType = "List<T>")
    private List<T> recordList;

    @ApiModelProperty(name = "count", value = "总数", dataType = "Integer")
    private Integer count;

}
