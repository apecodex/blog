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
 * @description: 前台友链
 * @author: apecode
 * @date: 2022-06-24 22:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台友链", description = "前台友链")
public class FriendLinkFrontDto {

    @ApiModelProperty(value = "创建者", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "友链名称", name = "linkName", dataType = "String")
    private String linkName;

    @ApiModelProperty(value = "友链头像", name = "linkAvatar", dataType = "String")
    private String linkAvatar;

    @ApiModelProperty(value = "友链介绍", name = "linkIntro", dataType = "String")
    private String linkIntro;

    @ApiModelProperty(value = "友链地址", name = "linkUrl", dataType = "String")
    private String linkUrl;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
