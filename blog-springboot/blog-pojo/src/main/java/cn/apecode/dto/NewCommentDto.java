package cn.apecode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "最新评论", description = "最新评论")
public class NewCommentDto {

    @ApiModelProperty(value = "主题id", name = "topicId", dataType = "String")
    private String topicId;

    @ApiModelProperty(value = "评论类型", name = "type", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(value = "评论用户", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = " 评论内容", name = "commentContent", dataType = "String")
    private String commentContent;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
