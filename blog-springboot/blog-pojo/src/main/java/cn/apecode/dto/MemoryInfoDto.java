package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 系统内存
 * @author: apecode
 * @date: 2022-08-24 01:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "系统内存", description = "系统内存")
public class MemoryInfoDto {

    @ApiModelProperty(value = "总内存", name = "total")
    private String total;
    @ApiModelProperty(value = "可用内存", name = "available")
    private String available;
    @ApiModelProperty(value = "已用内存", name = "used")
    private String used;
    @ApiModelProperty(value = "内存使用率", name = "ute")
    private String ute;
}
