package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description: 用户友情链接
 * @author: apecode
 * @date: 2022-06-24 21:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户友情链接", description = "用户友情链接")
public class FriendLinkUserVo {

    @ApiModelProperty(name = "id", value = "友链id", dataType = "String")
    private String id;

    @NotBlank(message = "友链名不能为空")
    @ApiModelProperty(name = "linkName", value = "友链名", dataType = "String", required = true)
    private String linkName;

    @NotBlank(message = "友链头像不能为空")
    @ApiModelProperty(name = "linkAvatar", value = "友链头像", dataType = "String", required = true)
    private String linkAvatar;

    @NotBlank(message = "友链地址不能为空")
    @ApiModelProperty(name = "linkUrl", value = "友链地址", dataType = "String", required = true)
    private String linkUrl;

    @ApiModelProperty(name = "linkIntro", value = "友链简介", dataType = "String")
    private String linkIntro;

}
