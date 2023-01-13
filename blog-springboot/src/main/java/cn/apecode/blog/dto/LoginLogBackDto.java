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
 * @description: 后台登录日志
 * @author: apecode
 * @date: 2022-07-10 16:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台登录日志", description = "后台登录日志")
public class LoginLogBackDto {

    @ApiModelProperty(value = "id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "用户id", name = "user", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto user;

    @ApiModelProperty(value = "登录类型", name = "loginType", dataType = "Integer")
    private Integer loginType;

    @ApiModelProperty(value = "登录ip", name = "ipAddress", dataType = "String")
    private String ipAddress;

    @ApiModelProperty(value = "IP来源", name = "ipSource", dataType = "String")
    private String ipSource;

    @ApiModelProperty(value = "经纬度", name = "rectangle", dataType = "String")
    private String rectangle;

    @ApiModelProperty(value = "浏览器信息", name = "browser", dataType = "String")
    private String browser;

    @ApiModelProperty(value = "操作系统", name = "os", dataType = "String")
    private String os;

    @ApiModelProperty(value = "创建时间", name = "", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
