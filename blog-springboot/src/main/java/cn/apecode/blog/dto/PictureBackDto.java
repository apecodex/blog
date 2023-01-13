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
 * @description: 后台图片
 * @author: apecode
 * @date: 2022-06-23 22:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台图片", description = "后台图片展示")
public class PictureBackDto {

    @ApiModelProperty(value = "图片id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "图片名称", name = "pictureName", dataType = "String")
    private String pictureName;

    @ApiModelProperty(value = "图片路径", name = "pictureSrc", dataType = "String")
    private String pictureSrc;

    @ApiModelProperty(value = "图片宽度", name = "width", dataType = "Integer")
    private Integer width;

    @ApiModelProperty(value = "图片高度", name = "height", dataType = "height")
    private Integer height;

    @ApiModelProperty(value = "图片大小", name = "size", dataType = "size")
    private String size;

    @ApiModelProperty(value = "逻辑删除(0不删除/1删除)", name = "isDelete", dataType = "Boolean")
    private Boolean isDelete;

    @ApiModelProperty(value = "创建者", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
