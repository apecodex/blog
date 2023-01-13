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
 * @description: 后台友链
 * @author: apecode
 * @date: 2022-06-24 20:31
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台友链", description = "后台友链展示")
public class FriendLinkBackDto {

    @ApiModelProperty(value = "友链id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "友联用户", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "友链名称", name = "linkName", dataType = "String")
    private String linkName;

    @ApiModelProperty(value = "友链头像", name = "linkAvatar", dataType = "String")
    private String linkAvatar;

    @ApiModelProperty(value = "友链简介", name = "linkIntro", dataType = "String")
    private String linkIntro;

    @ApiModelProperty(value = "友链地址", name = "linkUrl", dataType = "String")
    private String linkUrl;

    @ApiModelProperty(value = "审核(1审核/0待审)", name = "isReview", dataType = "Boolean")
    private Boolean isReview;

    @ApiModelProperty(value = "备注", name = "remark", dataType = "String")
    private String remark;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
