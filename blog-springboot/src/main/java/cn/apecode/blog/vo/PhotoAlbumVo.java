package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description: 相册
 * @author: apecode
 * @date: 2022-06-23 14:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "相册", description = "添加或修改相册")
public class PhotoAlbumVo {

    @ApiModelProperty(value = "相册id", name = "albumId", dataType = "String")
    private String albumId;

    @NotBlank(message = "相册名不能为空")
    @ApiModelProperty(value = "相册名称", name = "albumName", dataType = "String", required = true)
    private String albumName;

    @ApiModelProperty(value = "相册描述", name = "albumDesc", dataType = "String")
    private String albumDesc;

    @NotBlank(message = "相册封面不能为空")
    @ApiModelProperty(value = "相册封面", name = "albumCover", dataType = "String", required = true)
    private String albumCover;

    @ApiModelProperty(value = "是否公开(0否/1是)", name = "status", dataType = "Boolean")
    private Boolean status;

    @ApiModelProperty(value = "逻辑删除(0不删除/1删除)", name = "isDelete", dataType = "Boolean")
    private Boolean isDelete;
}
