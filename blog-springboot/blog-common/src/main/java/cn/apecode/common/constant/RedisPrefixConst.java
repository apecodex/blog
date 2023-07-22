package cn.apecode.common.constant;

/**
 * @description: Redis前缀名
 * @author: apecode
 * @date: 2022-05-29 20:17
 **/
public class RedisPrefixConst {

    // 用户详细信息
    public static final String USER_CACHE = "uc";

    // 验证码
    public static final String USER_CODE_KEY = "code:";

    // 验证码过期时间
    public static final long CODE_EXPIRE_TIME = 10 * 60;

    // 用户点赞
    public static final String ARTICLE_USER_LIKE = "aUser_like:";

    // 文章点赞数
    public static final String ARTICLE_LIKE_COUNT = "aLike_count";

    // 文章浏览数
    public static final String ARTICLE_VIEWS_COUNT = "aView_count";

    // 说说点赞用户
    public static final String TALK_USER_LIKE = "tUser_like:";

    // 说说点赞数
    public static final String TALK_LIKE_COUNT = "tLike_count";

    // 评论点赞用户
    public static final String COMMENT_USER_LIKE = "cUser_like:";

    // 评论点赞总量
    public static final String COMMENT_LIKE_COUNT = "cLike_count";

    // 日访问量
    public static final String DAY_VISITOR = "v_day";

    // 独立访问量
    public static final String UNIQUE_VISITOR = "v_only";

    // 访问所在地区
    public static final String VISITOR_AREA = "v_area";

    // 网站访问量
    public static final String BLOG_VIEWS_COUNT = "blog_view_count";

    // 关于我
    public static final String ABOUT_ME = "aboutMe";

    // 网站配置
    public static final String WEBSITE_CONFIG = "website_config";

    // 在线用户
    public static final String ONLINE_USER = "online_user";

}
