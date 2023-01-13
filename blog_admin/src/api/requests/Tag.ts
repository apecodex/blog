import request from '~/api/service'

/**
 * 修改标签
 * @param data
 */
export function updateTag(data: FormData): Promise<ResultObject<null>> {
    return request.put({
        url: '/admin/tags',
        data
    })
}

/**
 * 删除标签
 * @param data
 */
export function deleteTag(data: FormData): Promise<ResultObject<null>> {
    return request.delete({
        url: '/admin/tags',
        data
    })
}

/**
 * 后台标签列表
 * @param condition
 */
export function listTagBack(condition: ConditionParams): Promise<PageResult<Array<TagBackModel>>> {
    return request.get({
        url: '/admin/tags',
        params: condition
    })
}

/**
 * 保存标签
 * @param tags
 */
export function saveTag(tags: Array<string>): Promise<ResultObject<null>> {
    return request.post({
        url: '/admin/tags',
        data: tags
    })
}
