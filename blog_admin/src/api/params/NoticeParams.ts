/**
 * 获取后台消息参数
 */
declare type NoticeParams = {
  isRead: boolean
  isSystemNotice?: boolean
  nickname?: string,
  size?: number,
  current?: number
}

/**
 * 发送消息通知
 */
declare type SendNoticeParams = {
  title: string
  content: string
  url?: string
  userId: string
}