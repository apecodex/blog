/**
 * 用户登录参数
 */
declare type UserLoginParams = {
    email: string
    password: string
    captchaVerification: string
}

/**
 * QQ登录参数
 */
declare type QQLoginParams = {
    accessToken: string
    openId: string
}

/**
 * 修改用户信息
 */
declare type UserInfoParams = {
    intro: string
    isEmailNotice: boolean
    nickname: string
    webSite: string
}

/**
 * 绑定或解绑邮箱
 */
declare type BindOrUnBindEmail = {
    captchaVerification: string
    code: string
    email: string
}