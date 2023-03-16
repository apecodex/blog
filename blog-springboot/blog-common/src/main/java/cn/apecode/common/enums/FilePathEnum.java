package cn.apecode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 文件路径枚举
 * @author: apecode
 * @date: 2022-06-23 16:41
 **/
@Getter
@AllArgsConstructor
public enum FilePathEnum {

    AVATAR("avatar/", "头像路径"),
    ARTICLE("article/", "文章图片路径"),
    VOICE("voice/", "音频路径"),
    CONFIG("config/", "网站配置路径"),
    ALBUM("albums/","相册路径"),
    PICTURE_VIDEO("picture_video/", "说说图片视频路径");

    private final String path;
    private final String desc;
}
