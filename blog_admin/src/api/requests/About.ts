import request from '../service'

/**
 * 获取关于我
 */
export function getAboutMe(): Promise<ResultObject<string>> {
    return request.get({
        url: '/about'
    })
}

/**
 * 修改关于我
 * @param data
 */
export function updateAboutMe(data: FormData): Promise<ResultObject<null>> {
    return request.put({
        url: '/admin/about',
        data
    })
}