package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: IP地址信息
 * @author: apecode
 * @date: 2022-06-10 17:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "IP地址信息", description = "用户IP地址信息")
public class IpSourceDto {

    @ApiModelProperty(value = "国家")
    private String country;
    @ApiModelProperty(value = "省份")
    private String province;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "区域")
    private String district;
    @ApiModelProperty(value = "经纬度")
    private String location;
}
