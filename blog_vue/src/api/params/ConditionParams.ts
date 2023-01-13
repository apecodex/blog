/**
 * 查询参数
 */
declare interface ConditionParams {
  albumPath?: string,
  articleId?: string,
  categoryId?: string,
  email?: string,
  endTime?: string,
  startTime?: string,
  isDelete?: boolean | null | number,
  isReview?: boolean | number | null,
  keywords?: string,
  loginType?: number,
  size?: number,
  current?: number,
  status?: number | null,
  tagId?: string,
  type?: number | null,
}