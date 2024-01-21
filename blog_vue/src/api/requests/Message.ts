import {request} from "@/api/services"

/**
 * 获取留言列表
 * @param condition
 */
export function getMessages(condition: ConditionParams): Promise<PageResult<Array<MessageEntity>>> {
    return request.get({
        url: "/messages",
        params: condition,
        isDecrypt: true
    })
}

/**
 * 添加留言
 * @param data
 */
export function saveMessage(data: {content: string, theme: string}): Promise<ResultObject<null>> {
    return request.post({
        url: "/message",
        data,
        isEncrypt: true,
    })
}