import {request} from "@/api/services"

/**
 * 获取分类列表
 */
export function getCategories(): Promise<ResultObject<CategoryFrontEntity>> {
    return request.get({
        url: '/categories',
        isDecrypt: true
    })
}