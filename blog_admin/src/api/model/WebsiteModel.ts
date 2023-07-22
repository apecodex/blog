declare type WebsiteInfoModel = {
  // 文章数量
  articleCount: number
  // 分类数量
  categoryCount: number
  // 标签数量
  tagCount: number
  // 访问量
  viewsCount: number
  // 独立访问量
  onlyViewCount: number
  // 网站头像
  websiteAvatar: string
  // 网站名称
  websiteName: string
  // 网站作者
  websiteAuthor: string
  // 网站介绍
  websiteIntro: string
  // 网站公告
  websiteNotice: string
  // 网站创建时间
  websiteCreateTime: string
  // 网站备案号
  websiteRecordNo: string
  // 社交登录列表
  socialLoginList: string[]
  // 微信二维码
  weiXinQRCode: string
  // 支付宝二维码
  alipayQRCode: string
  // 是否自动播放音乐
  isAutoPlayer: boolean
}

// 用户访问区域统计
declare type VisitAreaStatisticsModel = {
  area: string
  count: number
}

// 文章统计
declare type ArticleStatisticsModel = {
  date: string
  count: number
}

// 用户7天访问量统计
declare type VisitStatisticsModel = {
  date: string
  viewsCount: number
}

// 文章浏览量排行
declare type ArticleRankModel = {
  articleTitle: string
  viewsCount: number
}

// 文章点赞排行
declare type ArticleLikeRankModel = {
  articleTitle: string
  likeCount: number
}

declare type SystemMemoryModel = {
  total: string
  available: string
  used: string
  ute: string
}

declare type SystemDiskInfoModel = {
  path: string
  free: string
  use: string
  total: string
}

declare type WebsiteInfoBackModel = {
  // 文章数量
  articleCount: number
  // 访问量
  viewsCount: number
  // 独立访问量
  onlyViewCount: number
  // 用户量
  userCount: number
  // 留言量
  messageCount: number
  // 分类量
  categoryCount: number
  // 标签量
  tagCount: number
  // 说说量
  talkCount: number
  // 未读通知量
  noReadNoticeCount: number
  // 用户访问区域统计列表
  userVisitAreaStatisticsList: VisitAreaStatisticsModel[]
  // 文章统计列表
  articleStatisticsList: ArticleStatisticsModel[]
  // 用户7天访问量统计列表
  visitStatisticsList: VisitStatisticsModel[]
  // 文章浏览量排行
  articleRankList: ArticleRankModel[]
  // 文章点赞
  articleLikeRankList: ArticleLikeRankModel[]
  categoryList: CategoryFrontModel[]
  tagsName: string[]
  systemMemory: SystemMemoryModel
  systemDiskInfo: SystemDiskInfoModel[]
}

// 网站配置
declare type WebsiteConfigureModel = {
  websiteAvatar: string
  websiteName: string
  websiteAuthor: string
  websiteIntro: string
  websiteNotice: string
  receiveEmail: string
  url: string
  websiteCreateTime: string
  websiteRecordNo: string
  websiteBackgroundImages: Array<string>
  homeTyperTexts: Array<string>
  socialLogin: {}
  qq: string
  wechat: string
  touristAvatar: string
  defaultAvatar: string
  isCommentReview: boolean
  isMessageReview: boolean
  isEmailNotice: boolean
  isMessageNotice: boolean
  weiXinQRCode: string
  alipayQRCode: string
  isAutoPlayer: boolean
  chatRoomNotice: string
}