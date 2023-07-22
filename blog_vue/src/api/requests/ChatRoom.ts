import { request } from '@/api/services'

// 获取聊天室游客信息
export function getChatVisitorInfo(): Promise<ResultObject<VisitorUser>> {
  return request.get({
    url: '/getChatVisitorInfo'
  })
}

// 获取在线用户列表
export function getOnlineUserList(): Promise<ResultObject<Array<OnlineUser>>> {
  return request.get({
    url: '/getOnlineUserList'
  })
}