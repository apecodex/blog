/**
 * 保存或更新文章参数
 */
declare type OnlyArticleBackModel  = {
  id: string
  articleTitle: string
  categoryName: null
  articleContent: string
  articleCover: string
  isTop: boolean
  originalUrl: string
  status: number
  tagNameList: string[]
  type: number
}

// 后台文章信息
declare type ArticleBackModel  = {
  id: string
  author: SimpleUserInfoModel
  articleTitle: string
  categoryName: string
  articleCover: string
  likeCount: number
  viewsCount: number
  originalUrl: string
  tagNames: string[]
  type: number
  isTop: boolean
  status: number
  isDelete: boolean
  createTime: string
  updateTime: string
}