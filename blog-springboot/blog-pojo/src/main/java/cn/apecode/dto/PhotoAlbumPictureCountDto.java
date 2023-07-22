package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 相册图片的数量
 * @author: apecode
 * @date: 2023-07-22 18:05
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "相册图片的数量", description = "相册图片的数量")
public class PhotoAlbumPictureCountDto {

    @ApiModelProperty(value = "相册id", name = "albumId", dataType = "Integer")
    private Integer albumId;
    @ApiModelProperty(value = "图片数量", name = "count", dataType = "Integer")
    private Integer count;

}
