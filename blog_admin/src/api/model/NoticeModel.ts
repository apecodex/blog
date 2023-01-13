/**
 * 消息实体
 */
declare type NoticeBackModel = {
  id: string
  user: SimpleUserInfoModel
  title: string
  content: string
  url: string
  status: boolean
  createTime: string
}