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
 * @description: 前台相册
 * @author: apecode
 * @date: 2022-06-23 18:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台相册", description = "前台相册展示")
public class PhotoAlbumFrontDto {

    @ApiModelProperty(value = "相册id", name = "id", dataType = "String", hidden = true)
    private String id;

    @ApiModelProperty(value = "相册名称", name = "albumName", dataType = "String")
    private String albumName;

    @ApiModelProperty(value = "相册封面", name = "albumCover", dataType = "String")
    private String albumCover;

    @ApiModelProperty(value = "图片数量", name = "pictureCount", dataType = "Integer")
    private Integer pictureCount;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
