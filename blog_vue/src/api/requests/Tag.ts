import {request} from "@/api/services"

/**
 * 获取标签列表
 * @param condition
 */
export function getTags(condition: ConditionParams): Promise<ResultObject<TagFrontEntity>> {
    return request.get({
        url: "/tags",
        params: condition,
        isDecrypt: true
    })
}