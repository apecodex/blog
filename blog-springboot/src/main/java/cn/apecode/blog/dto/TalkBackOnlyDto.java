package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 后台说说
 * @author: apecode
 * @date: 2022-09-12 14:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台说说", description = "根据说说id获取")
public class TalkBackOnlyDto {

    @ApiModelProperty(value = "说说id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "说说内容", name = "content", dataType = "String")
    private String content;

    @ApiModelProperty(value = "是否顶置", name = "isTop", dataType = "Boolean")
    private Boolean isTop;

    @ApiModelProperty(value = "状态(1公开/2私密/3草稿)", name = "status", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(value = "配图", name = "src", dataType = "List<TalkPictureVideoDto>")
    private List<TalkPictureVideoDto> pictureVideos;

}
