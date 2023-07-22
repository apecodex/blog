/**
 * 用户登录参数
 */
declare type UserLoginParams = {
  username: string
  password: string
  captchaVerification: string
}

/**
 * 修改密码
 */
declare type UpdatePassword = {
  oldPassword: string | null
  newPassword: string | null
  code: string | null
  captchaVerification: string
}

/**
 * 更新用户信息
 */
declare type UpdateUserInfo = {
  nickname: string | null
  intro: string | null
  webSite: string | null
  isEmailNotice: boolean | null
}
/**
 * 更新用户禁用状态
 */
declare type UserEnable = {
  id: string
  enable: boolean
}