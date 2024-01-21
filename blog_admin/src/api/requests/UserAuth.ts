import request from '../service'

/**
 * 登录
 * @param data
 */
export function Login(data: FormData): Promise<ResultObject<UserInfoModel>> {
  return request.post({
    headers: { 'Content-type': 'application/x-www-form-urlencoded' },
    url: "/login",
    data
  })
}

/**
 * 获取后台用户列表
 * @param condition
 */
export function listUserBack(condition: ConditionParams): Promise<PageResult<Array<UsersBackModel>>> {
  return request.get({
    url: '/admin/users',
    params: condition,
    isDecrypt: true
  })
}

/**
 * 修改用户禁用状态
 * @param data
 */
export function updateUserDisabledStatus(data: UserEnable): Promise<ResultObject<null>> {
  return request.put({
    url: '/admin/user/enable',
    data,
    isEncrypt: true
  })
}

/**
 * 发送邮件
 * @param data
 */
export function sendEmail(data: { email: string }): Promise<ResultObject<null>> {
  return request.get({
    url: '/user/code',
    params: data
  })
}

/**
 * 修改用户密码
 * @param data
 */
export function updateUserPassword(data: UpdatePassword): Promise<ResultObject<null>> {
  return request.put({
    url: '/user/password',
    data,
    isEncrypt: true
  })
}