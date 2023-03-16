package cn.apecode.vo;

import cn.apecode.dto.UploadFileInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 图片
 * @author: apecode
 * @date: 2022-06-23 23:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "图片", description = "图片")
public class PictureVo {

    @NotNull(message = "相册id不能为空")
    @ApiModelProperty(name = "albumId", value = "相册id", required = true, dataType = "String")
    private String albumId;

    @ApiModelProperty(name = "pictureUrlList", value = "照片url", required = true, dataType = "List<String>")
    private List<UploadFileInfoDto> pictureUrlList;

    @ApiModelProperty(name = "pictureIdList", value = "照片id", required = true, dataType = "List<String>")
    private List<String> pictureIdList;

}
