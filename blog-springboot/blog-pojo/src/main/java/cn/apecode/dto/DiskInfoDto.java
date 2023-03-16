package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 硬盘信息
 * @author: apecode
 * @date: 2022-08-24 01:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "硬盘信息", description = "硬盘信息")
public class DiskInfoDto {

    @ApiModelProperty(value = "路径", name = "path")
    private String path;
    @ApiModelProperty(value = "空闲空间", name = "free")
    private String free;
    @ApiModelProperty(value = "可用空间", name = "use")
    private String use;
    @ApiModelProperty(value = "总空间", name = "total")
    private String total;

}
