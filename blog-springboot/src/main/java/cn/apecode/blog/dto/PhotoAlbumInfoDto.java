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
 * @description: 前台相册信息
 * @author: apecode
 * @date: 2022-11-13 16:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台相册信息", description = "前台相册信息")
public class PhotoAlbumInfoDto {

    @ApiModelProperty(value = "相册名称", name = "albumName", dataType = "String")
    private String albumName;

    @ApiModelProperty(value = "相册描述", name = "albumDesc", dataType = "String")
    private String albumDesc;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
