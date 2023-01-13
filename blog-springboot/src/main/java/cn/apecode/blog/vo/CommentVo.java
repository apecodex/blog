package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 评论
 * @author: apecode
 * @date: 2022-07-07 18:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "评论", description = "评论")
public class CommentVo {

    @ApiModelProperty(value = "主题id", name = "topicId", dataType = "String")
    private String topicId;

    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty(value = "评论内容", name = "commentContent", required = true, dataType = "String")
    private String commentContent;

    @ApiModelProperty(value = "父id", name = "parentId", dataType = "String")
    private String parentId;

    @ApiModelProperty(value = "回复评论id", name = "replyCommentId", dataType = "String")
    private String replyCommentId;

    @NotNull(message = "评论类型不能为空")
    @ApiModelProperty(value = "评论类型", name = "type", dataType = "Integer")
    private Integer type;
}
