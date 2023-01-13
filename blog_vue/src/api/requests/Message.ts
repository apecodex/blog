import {request} from "@/api/services"

/**
 * 获取留言列表
 * @param condition
 */
export function getMessages(condition: ConditionParams): Promise<PageResult<Array<MessageEntity>>> {
    return request.get({
        url: "/messages",
        params: condition
    })
}

/**
 * 添加留言
 * @param data
 */
export function saveMessage(data: FormData): Promise<ResultObject<null>> {
    return request.post({
        url: "/message",
        data
    })
}