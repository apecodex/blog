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
 * @description: 前台图片
 * @author: apecode
 * @date: 2022-06-24 13:47
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台图片", description = "前台图片展示")
public class PictureFrontDto {

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

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
