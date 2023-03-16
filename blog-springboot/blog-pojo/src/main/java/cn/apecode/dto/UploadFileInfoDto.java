package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @description: 上传的文件信息
 * @author: apecode
 * @date: 2022-11-15 13:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@ApiModel(value = "上传的文件信息", description = "上传的文件信息")
public class UploadFileInfoDto {

    @ApiModelProperty(value = "图片宽度", name = "width", dataType = "Integer")
    private Integer width;

    @ApiModelProperty(value = "图片高度", name = "height", dataType = "Integer")
    private Integer height;

    @ApiModelProperty(value = "文件大小", name = "size", dataType = "String")
    private String size;

    @ApiModelProperty(value = "文件地址", name = "url", dataType = "String")
    private String url;
}
