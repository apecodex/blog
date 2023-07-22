
declare type ChatMessage<T> = {
  sender: SimpleUserInfoEntity
  type: string,
  time: Date,
  ipSource: string,
  data: T
}

// 发送消息
declare type SnedMessage<T> = {
  uid: string,
  message: T
}

// 在线用户
declare type OnlineUser = {
  uid: string
  nickname: string
  avatar: string
  ipAddress: string
  ipSource: string
}