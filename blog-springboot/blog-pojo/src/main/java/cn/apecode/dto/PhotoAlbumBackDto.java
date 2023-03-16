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
 * @description: 后台相册
 * @author: apecode
 * @date: 2022-06-23 13:46
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台相册", description = "后台相册展示")
public class PhotoAlbumBackDto {

    @ApiModelProperty(value = "相册id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "创建者", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "相册名称", name = "albumName", dataType = "String")
    private String albumName;

    @ApiModelProperty(value = "相册描述", name = "albumDesc", dataType = "String")
    private String albumDesc;

    @ApiModelProperty(value = "相册封面", name = "albumCover", dataType = "String")
    private String albumCover;

    @ApiModelProperty(value = "图片数量", name = "pictureCount", dataType = "Integer")
    private Integer pictureCount;

    @ApiModelProperty(value = "是否公开(0否/1是)", name = "status", dataType = "Boolean")
    private Boolean status;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
