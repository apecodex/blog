import {request} from '@/api/services'

/**
 * 登录
 * @param data
 */
export function Login(data: FormData): Promise<ResultObject<UserEntity>> {
    return request.post({
        url: "/login",
        data
    })
}

/**
 * 注册
 * @param data
 */
export function register(data: {code: string, email: string, password: string, captchaVerification: string}): Promise<ResultObject<null>> {
    return request.post({
        url: "/register",
        data,
        isEncrypt: true
    })
}

/**
 * 发送邮箱验证码
 * @param email
 */
export function sendEmailCode(email: string): Promise<ResultObject<null>> {
    return request.get({
        url: "/user/code",
        params: {email}
    })
}

/**
 * 找回密码
 * @param data
 */
export function findPassword(data: {code: string, email: string, newPassword: string, captchaVerification: string}): Promise<ResultObject<null>> {
    return request.put({
        url: "/findPassword",
        data,
        isEncrypt: true
    })
}

/**
 * 更新用户头像
 * @param file
 */
export function updateUserAvatar(file: FormData): Promise<ResultObject<UploadFileInfoModel>> {
    return request.post({
        url: '/user/avatar',
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        data: file
    })
}

/**
 * 获取用户信息
 */
export function getUserInfo(): Promise<ResultObject<UserInfoModel>> {
    return request.get({
        url: "/user/info",
        isDecrypt: true
    })
}

/**
 * 修改用户信息
 * @param data
 */
export function updateUserInfo(data: UserInfoParams): Promise<ResultObject<null>> {
    return request.put({
        url: "/user/info",
        data,
        isEncrypt: true
    })
}

/**
 * 修改用户密码
 * @param data
 */
export function updateUserPassword(data: UpdatePasswordParams): Promise<ResultObject<null>> {
    return request.put({
        url: "/user/password",
        data,
        isEncrypt: true
    })
}

/**
 * QQ登录
 * @param data
 */
export function qqLogin(data: QQLoginParams): Promise<ResultObject<UserEntity>> {
    return request.post({
        url: "/oauth/qq/callback",
        data
    })
}

/**
 * 绑定QQ
 * @param data
 */
export function bindQQ(data: QQLoginParams): Promise<ResultObject<null>> {
    return request.post({
        url: "/user/qq/bind",
        data
    })
}

/**
 * 解绑QQ
 */
export function unbindQQ(): Promise<ResultObject<null>> {
    return request.put({
        url: "/user/qq/unbind",
    })
}

/**
 * 绑定邮箱
 * @param data
 */
export function bindEmail(data: BindOrUnBindEmail): Promise<ResultObject<string>> {
    return request.post({
        url: "/user/email/bind",
        data
    })
}
/**
 * 解绑邮箱
 * @param data
 */
export function unbindEmail(data: BindOrUnBindEmail): Promise<ResultObject<string>> {
    return request.put({
        url: "/user/email/unbind",
        data,
        isEncrypt: true
    })
}