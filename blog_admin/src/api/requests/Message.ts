import request from '~/api/service'

/**
 * 后台留言列表
 * @param condition
 */
export function listMessageBack(condition: ConditionParams): Promise<PageResult<Array<MessageBackModel>>> {
    return request.get({
        url: '/admin/messages',
        params: condition,
        isDecrypt: true
    })
}

/**
 * 更新留言审核
 * @param data
 */
export function updateMessageReview(data: { idList: Array<string>, isReview: boolean }): Promise<ResultObject<null>> {
    return request.put({
        url: '/admin/message',
        data,
        isEncrypt: true
    })
}

/**
 * 删除留言
 * @param data
 */
export function deleteMessage(data: Array<string>): Promise<ResultObject<null>> {
    return request.delete({
        url: '/admin/message',
        data,
        isEncrypt: true
    })
}