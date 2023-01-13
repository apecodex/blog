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