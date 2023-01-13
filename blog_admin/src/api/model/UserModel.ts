/**
 * 管理员登录后返回的信息
 */
declare type UserInfoModel = {
  nickname: string
  uid: string
  email: string
  avatar: string
  articleLikeSet: Array<string>
  commentLikeSet: Array<string>
  talkLikeSet: Array<string>
  token: string
  tokenHead: string
}

/**
 * 部分用户信息
 * uid: 8位数id
 * nickname: 用户名
 * avatar: 用户头像
 * intro: 用户简介
 * webSite: 用户网站
 */
declare type SimpleUserInfoModel = {
  uid: string
  nickname: string
  avatar: string
  intro: string | null
  webSite: string | null
}

// 后台用户
declare type UsersBackModel = {
  userAuthId: string,
  userInfoId: string,
  uid: string,
  nickname: string,
  email: string,
  avatar: string,
  intro: string | null,
  webSite: string | null,
  bindQQ: boolean,
  isEmailNotice: boolean,
  createTime: string,
  enable: boolean,
  loginType: number,
  roleList: Array<roleOptionModel>,
  ipAddress: string,
  ipSource: string,
  rectangle: string | null,
  lastLoginTime: string
}