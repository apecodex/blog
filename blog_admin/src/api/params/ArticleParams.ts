/**
 * 保存或更新文章参数
 */
declare type SaveOrUpdateArticleParams = {
  id?: string
  articleTitle: string
  categoryName: null
  articleContent: string
  articleCover: string
  isTop: boolean
  originalUrl?: string
  status: number
  tagNameList: string[]
  type: number
}