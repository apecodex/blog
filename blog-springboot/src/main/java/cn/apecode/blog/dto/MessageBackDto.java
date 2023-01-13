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
 * @description: 后台留言
 * @author: apecode
 * @date: 2022-06-24 16:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台留言", description = "后台留言展示")
public class MessageBackDto {

    @ApiModelProperty(value = "主键", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "留言者", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "昵称", name = "nickname", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "头像", name = "avatar", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "内容", name = "content", dataType = "String")
    private String content;

    @ApiModelProperty(value = "留言主题样式", name = "theme", dataType = "String")
    private String theme;

    @ApiModelProperty(value = "审核", name = "isReview", dataType = "Boolean")
    private Boolean isReview;

    @ApiModelProperty(value = "IP地址", name = "ipAddress", dataType = "String")
    private String ipAddress;

    @ApiModelProperty(value = "IP来源", name = "ipSource", dataType = "String")
    private String ipSource;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
