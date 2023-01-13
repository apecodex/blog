import request from '~/api/service'

/**
 * 获取操作日志
 * @param condition
 */
export function listOperationBack(condition: ConditionParams): Promise<PageResult<Array<OperationLogBack>>> {
    return request.get({
        url: '/admin/operation/logs',
        params: condition
    })
}

/**
 * 删除操作日志
 * @param data
 */
export function deleteOperation(data: {idList: Array<string>, isDelete: boolean}): Promise<ResultObject<null>> {
    return request.delete({
        url: '/admin/operation/logs',
        data
    })
}

/**
 * 获取登录日志
 * @param condition
 */
export function listLoginLogBack(condition: ConditionParams): Promise<PageResult<Array<LoginLogBack>>> {
    return request.get({
        url: '/admin/login/logs',
        params: condition
    })
}

/**
 * 删除登录日志
 * @param data
 */
export function deleteLoginLog(data: {idList: Array<string>, isDelete: boolean}): Promise<ResultObject<null>> {
    return request.delete({
        url: '/admin/login/logs',
        data
    })
}

/**
 * 获取邮件日志
 * @param condition
 */
export function listMailLogBack(condition: ConditionParams): Promise<PageResult<Array<MailLogBack>>> {
    return request.get({
        url: '/admin/email/logs',
        params: condition
    })
}

/**
 * 删除邮件日志
 * @param data
 */
export function deleteMailLog(data: {idList: Array<string>, isDelete: boolean}): Promise<ResultObject<null>> {
    return request.delete({
        url: '/admin/email/logs',
        data
    })
}