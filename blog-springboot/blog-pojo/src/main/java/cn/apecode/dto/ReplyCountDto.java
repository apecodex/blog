package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 回复数量
 * @author: apecode
 * @date: 2022-07-09 00:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "回复数量", description = "回复数量")
public class ReplyCountDto {

    @ApiModelProperty(value = "评论id", name = "commentId", dataType = "String")
    private String commentId;

    @ApiModelProperty(value = "回复数量", name = "replyCount", dataType = "Integer")
    private Integer replyCount;

}
