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