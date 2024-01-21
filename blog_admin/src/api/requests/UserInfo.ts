import request from '~/api/service'

/**
 * 修改用户角色
 * @param data
 */
export function updateUserRole(data: { roleList: Array<string>, userId: string }): Promise<ResultObject<null>> {
    return request.put({
        url: '/admin/user/role',
        data,
        isEncrypt: true
    })
}

/**
 * 获取用户信息
 */
export function getUserInfo(): Promise<ResultObject<UserPersonalInfoModel>> {
    return request.get({
        url: '/user/info',
        isDecrypt: true
    })
}

/**
 * 更新用户头像
 * @param file
 */
export function updateUserAvatar(file: any): Promise<ResultObject<UploadFileInfoModel>> {
    return request.post({
        url: '/user/avatar',
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        data: file
    })
}

/**
 * 更新用户信息
 * @param data
 */
export function updateUserInfo(data: UpdateUserInfo): Promise<ResultObject<null>> {
    return request.put({
        url: '/user/info',
        data,
        isEncrypt: true
    })
}