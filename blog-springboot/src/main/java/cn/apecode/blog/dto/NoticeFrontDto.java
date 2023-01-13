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
 * @description: 通知
 * @author: apecode
 * @date: 2022-07-16 00:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台通知", description = "前台通知")
public class NoticeFrontDto {

    @ApiModelProperty(value = "通知id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "通知标题", name = "title", dataType = "String")
    private String title;

    @ApiModelProperty(value = "通知内容", name = "content", dataType = "String")
    private String content;

    @ApiModelProperty(value = "跳转的url", name = "url", dataType = "String")
    private String url;

    @ApiModelProperty(value = "是否已阅", name = "status", dataType = "Boolean")
    private Boolean status;

    @ApiModelProperty(value = "通知时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
