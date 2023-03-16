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
 * @description: 前台说说
 * @author: apecode
 * @date: 2022-07-07 16:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台说说", description = "前台说说展示")
public class TalkFrontDto {

    @ApiModelProperty(value = "用户", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "说说id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "说说内容", name = "content", dataType = "String")
    private String content;

    @ApiModelProperty(value = "是否顶置", name = "isTop", dataType = "Boolean")
    private Boolean isTop;

    @ApiModelProperty(value = "配图", name = "src", dataType = "List<String>")
    private List<String> src;

    @ApiModelProperty(value = "点赞数", name = "likeCount", dataType = "Integer")
    private Integer likeCount;

    @ApiModelProperty(value = "评论数", name = "commentCount", dataType = "Integer")
    private Integer commentCount;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "当前访问用户的经纬度", name = "rectangle", dataType = "String")
    private String rectangle;
}
