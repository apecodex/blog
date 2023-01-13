package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 审核
 * @author: apecode
 * @date: 2022-06-24 18:39
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "审核", description = "审核留言和评论")
public class ReviewVo {

    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<String>")
    private List<String> idList;

    @NotNull(message = "状态值不能为空")
    @ApiModelProperty(name = "isReview", value = "审核状态", required = true, dataType = "Boolean")
    private Boolean isReview;

    @ApiModelProperty(name = "remark", value = "备注", dataType = "String")
    private String remark;
}
