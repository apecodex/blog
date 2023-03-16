package cn.apecode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 前台评论
 * @author: apecode
 * @date: 2022-07-08 00:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台评论", description = "前台评论展示")
public class CommentFrontDto {

    @ApiModelProperty(value = "评论id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "评论者", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "内容", name = "commentContent", dataType = "String")
    private String commentContent;

    @ApiModelProperty(value = "距离", name = "distance", dataType = "String")
    private String distance;

    @ApiModelProperty(value = "访问浏览器", name = "browser", dataType = "String")
    private String browser;

    @ApiModelProperty(value = "操作系统", name = "os", dataType = "String")
    private String os;

    @ApiModelProperty(value = "回复数量", name = "replyCount", dataType = "Integer")
    private Integer replyCount;

    @ApiModelProperty(value = "点赞数量", name = "likeCount", dataType = "Integer")
    private Integer likeCount;

    @ApiModelProperty(value = "评论时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "回复列表", name = "replyList", dataType = "List<ReplyDto>")
    private List<ReplyDto> replyList;

}
