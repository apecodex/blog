package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @description: 博客首页信息
 * @author: apecode
 * @date: 2022-07-14 17:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "博客首页信息", description = "博客首页信息")
public class BlogHomeInfoDto {

    @ApiModelProperty(value = "文章数量", dataType = "Integer")
    private Integer articleCount;

    @ApiModelProperty(value = "分类数量", dataType = "Integer")
    private Integer categoryCount;

    @ApiModelProperty(value = "标签数量", dataType = "Integer")
    private Integer tagCount;

    @ApiModelProperty(value = "访问量", dataType = "Integer")
    private Integer viewsCount;

    @ApiModelProperty(value = "独立用户访问量", dataType = "Integer")
    private Integer onlyViewCount;

    @ApiModelProperty(value = "网站头像", name = "websiteAvatar", dataType = "String")
    private String websiteAvatar;

    @ApiModelProperty(value = "网站名称", name = "websiteName", dataType = "String")
    private String websiteName;

    @ApiModelProperty(value = "网站作者", name = "websiteAuthor", dataType = "String")
    private String websiteAuthor;

    @ApiModelProperty(value = "网站介绍", name = "websiteIntro", dataType = "String")
    private String websiteIntro;

    @ApiModelProperty(value = "网站公告", name = "websiteNotice", dataType = "String")
    private String websiteNotice;

    @ApiModelProperty(value = "网站创建时间", name = "websiteCreateTime", dataType = "String")
    private String websiteCreateTime;

    @ApiModelProperty(value = "网站备案号", name = "websiteRecordNo", dataType = "String")
    private String websiteRecordNo;

    @ApiModelProperty(name = "websiteBackgroundImages", value = "首页背景图", required = true, dataType = "String")
    private List<String> websiteBackgroundImages;

    @ApiModelProperty(name = "HomeTyperTexts", value = "打字机文本", required = true, dataType = "List<String>")
    private List<String> HomeTyperTexts;

    @ApiModelProperty(value = "社交登录列表", name = "socialLogin", dataType = "Map<String, Boolean>")
    private Map<String, Boolean> socialLogin;

    @ApiModelProperty(name = "qq", value = "qq号", required = true, dataType = "String")
    private String qq;

    @ApiModelProperty(name = "wechat", value = "微信号", required = true, dataType = "String")
    private String wechat;

    @ApiModelProperty(name = "touristAvatar", value = "游客头像", required = true, dataType = "String")
    private String touristAvatar;

    @ApiModelProperty(name = "defaultAvatar", value = "用户默认头像", required = true, dataType = "String")
    private String defaultAvatar;

    @ApiModelProperty(value = "微信二维码", name = "weiXinQRCode", dataType = "String")
    private String weiXinQRCode;

    @ApiModelProperty(value = "支付宝二维码", name = "alipayQRCode", dataType = "String")
    private String alipayQRCode;

    @ApiModelProperty(name = "isCommentReview", value = "是否评论审核", required = true, dataType = "Boolean")
    private Boolean isCommentReview;

    @ApiModelProperty(name = "isMessageReview", value = "是否留言审核", required = true, dataType = "Boolean")
    private Boolean isMessageReview;

    @ApiModelProperty(value = "是否自动播放音乐", name = "isMusicPlayer", dataType = "Boolean")
    private Boolean isAutoPlayer;

    @ApiModelProperty(name = "chatRoomNotice", value = "聊天室公告", required = true, dataType = "String")
    private String chatRoomNotice;

}
