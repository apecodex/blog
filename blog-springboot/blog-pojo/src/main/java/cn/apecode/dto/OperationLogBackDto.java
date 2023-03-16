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
 * @description: 后台操作日志
 * @author: apecode
 * @date: 2022-07-10 16:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台操作日志", description = "后台操作日志")
public class OperationLogBackDto {

    @ApiModelProperty(value = "id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "操作模块", name = "optModule", dataType = "String")
    private String optModule;

    @ApiModelProperty(value = "操作类型", name = "optType", dataType = "String")
    private String optType;

    @ApiModelProperty(value = "操作url", name = "optUrl", dataType = "String")
    private String optUrl;

    @ApiModelProperty(value = "操作方法", name = "optMethod", dataType = "String")
    private String optMethod;

    @ApiModelProperty(value = "操作描述", name = "optDesc", dataType = "String")
    private String optDesc;

    @ApiModelProperty(value = "请求参数", name = "requestParam", dataType = "String")
    private String requestParam;

    @ApiModelProperty(value = "请求方式", name = "requestMethod", dataType = "String")
    private String requestMethod;

    @ApiModelProperty(value = "返回数据", name = "responseData", dataType = "String")
    private String responseData;

    @ApiModelProperty(value = "用户id", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "操作ip", name = "idAddress", dataType = "String")
    private String idAddress;

    @ApiModelProperty(value = "操作地址", name = "ipSource", dataType = "String")
    private String ipSource;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
