package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @description: 网站配置
 * @author: apecode
 * @date: 2022-07-14 15:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "网站配置", description = "网站配置")
public class WebsiteConfigVo {

    @ApiModelProperty(name = "websiteAvatar", value = "网站头像", required = true, dataType = "String")
    private String websiteAvatar;

    @ApiModelProperty(name = "websiteName", value = "网站名称", required = true, dataType = "String")
    private String websiteName;

    @ApiModelProperty(name = "websiteAuthor", value = "网站作者", required = true, dataType = "String")
    private String websiteAuthor;

    @ApiModelProperty(name = "websiteIntro", value = "网站介绍", required = true, dataType = "String")
    private String websiteIntro;

    @ApiModelProperty(name = "websiteNotice", value = "网站公告", required = true, dataType = "String")
    private String websiteNotice;

    @ApiModelProperty(name = "websiteNotice", value = "网站通知接收邮箱", required = true, dataType = "String")
    private String receiveEmail;

    @ApiModelProperty(name = "url", value = "网站前台域名", required = true, dataType = "String", notes = "邮件通知使用")
    private String url;

    @ApiModelProperty(name = "websiteCreateTime", value = "网站创建时间", required = true, dataType = "String")
    private String websiteCreateTime;

    @ApiModelProperty(name = "websiteRecordNo", value = "网站备案号", required = true, dataType = "String")
    private String websiteRecordNo;

    @ApiModelProperty(name = "websiteBackgroundImages", value = "首页背景图", required = true, dataType = "String")
    private List<String> websiteBackgroundImages;

    @ApiModelProperty(name = "HomeTyperTexts", value = "打字机文本", required = true, dataType = "List<String>")
    private List<String> HomeTyperTexts;

    @ApiModelProperty(name = "socialLogin", value = "社交登录列表", required = true, dataType = "Map<String,  Boolean>")
    private Map<String,  Boolean> socialLogin;

    @ApiModelProperty(name = "qq", value = "qq号", required = true, dataType = "String")
    private String qq;

    @ApiModelProperty(name = "wechat", value = "微信号", required = true, dataType = "String")
    private String wechat;

    @ApiModelProperty(name = "touristAvatar", value = "游客头像", required = true, dataType = "String")
    private String touristAvatar;

    @ApiModelProperty(name = "defaultAvatar", value = "用户默认头像", required = true, dataType = "String")
    private String defaultAvatar;

    @ApiModelProperty(name = "isCommentReview", value = "是否评论审核", required = true, dataType = "Boolean")
    private Boolean isCommentReview;

    @ApiModelProperty(name = "isMessageReview", value = "是否留言审核", required = true, dataType = "Boolean")
    private Boolean isMessageReview;

    @ApiModelProperty(name = "isEmailNotice", value = "是否邮箱通知", required = true, dataType = "Boolean")
    private Boolean isEmailNotice;

    @ApiModelProperty(name = "isMessageNotice", value = "是否开启留言通知", required = true, dataType = "Boolean")
    private Boolean isMessageNotice;

    @ApiModelProperty(name = "weiXinQRCode", value = "微信二维码", required = true, dataType = "String")
    private String weiXinQRCode;

    @ApiModelProperty(name = "alipayQRCode", value = "支付宝二维码", required = true, dataType = "String")
    private String alipayQRCode;

    @ApiModelProperty(name = "isMusicPlayer", value = "是否自动播放音乐", required = true, dataType = "Boolean")
    private Boolean isAutoPlayer;
}
