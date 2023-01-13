package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 图片信息
 * @author: apecode
 * @date: 2022-06-23 23:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "图片信息", description = "图片信息")
public class PictureInfoVo {

    @NotNull(message = "图片路径不能为空")
    @ApiModelProperty(name = "picturePath", value = "图片路径", required = true, dataType = "String")
    private String picturePath;

    @NotBlank(message = "图片名称不能为空")
    @ApiModelProperty(name = "pictureName", value = "图片名称", required = true, dataType = "String")
    private String pictureName;

}
