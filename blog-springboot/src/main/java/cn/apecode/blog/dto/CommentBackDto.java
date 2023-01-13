package cn.apecode.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description: 后台评论
 * @author: apecode
 * @date: 2022-07-09 21:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台评论", description = "后台评论展示")
public class CommentBackDto {

    @ApiModelProperty(value = "评论id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "评论者", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "被回复用户", name = "replyUser", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto replyUser;

    @ApiModelProperty(value = "内容", name = "commentContent", dataType = "String")
    private String commentContent;

    @ApiModelProperty(value = "父id", name = "parentId", dataType = "String")
    private String parentId;

    @ApiModelProperty(value = "文章标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "回复数量", name = "replyCount", dataType = "String")
    private Integer replyCount;

    @ApiModelProperty(value = "点赞数量", name = "likeCount", dataType = "String")
    private Integer likeCount;

    @ApiModelProperty(value = "评论类型", name = "type", dataType = "String")
    private String type;

    @ApiModelProperty(value = "评论来源", name = "ipSource", dataType = "String")
    private String ipSource;

    @ApiModelProperty(value = "访问浏览器", name = "browser", dataType = "String")
    private String browser;

    @ApiModelProperty(value = "操作系统", name = "os", dataType = "String")
    private String os;

    @ApiModelProperty(value = "状态", name = "isReview", dataType = "Boolean")
    private Boolean isReview;

    @ApiModelProperty(value = "评论时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
