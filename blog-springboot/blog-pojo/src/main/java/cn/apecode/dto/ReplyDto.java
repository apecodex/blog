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
 * @description: 回复
 * @author: apecode
 * @date: 2022-07-08 00:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "回复", description = "回复展示")
public class ReplyDto {

    @ApiModelProperty(value = "评论id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "父评论id", name = "parentId", dataType = "String")
    private String parentId;

    @ApiModelProperty(value = "被回复评论id", name = "replyCommentId", dataType = "String")
    private String replyCommentId;

    @ApiModelProperty(value = "回复者", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "被回复用户", name = "replyUser", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto replyUser;

    @ApiModelProperty(value = "内容", name = "commentContent", dataType = "String")
    private String commentContent;

    @ApiModelProperty(value = "距离", name = "distance", dataType = "String")
    private String distance;

    @ApiModelProperty(value = "访问浏览器", name = "browser", dataType = "String")
    private String browser;

    @ApiModelProperty(value = "操作系统", name = "os", dataType = "String")
    private String os;

    @ApiModelProperty(value = "点赞数量", name = "likeCount", dataType = "Integer")
    private Integer likeCount;

    @ApiModelProperty(value = "评论时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
