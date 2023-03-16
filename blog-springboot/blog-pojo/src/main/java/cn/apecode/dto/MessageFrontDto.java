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
 * @description: 前台留言
 * @author: apecode
 * @date: 2022-06-24 18:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台留言", description = "前台留言展示")
public class MessageFrontDto {

    @ApiModelProperty(value = "留言者")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "昵称", name = "nickname", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "头像", name = "avatar", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "内容", name = "content", dataType = "String")
    private String content;

    @ApiModelProperty(value = "留言主题样式", name = "theme", dataType = "String")
    private String theme;

    @ApiModelProperty(value = "创建时间", name = "LocalDateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
