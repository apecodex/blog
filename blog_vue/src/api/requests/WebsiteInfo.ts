import {request} from '@/api/services'

/**
 * 获取博客信息
 */
export function getBlogInfo(): Promise<ResultObject<WebsiteInfoEntity>> {
    return request.get({
        url: '/',
        isDecrypt: true
    })
}

/**
 * 获取关于我
 */
export function getAbout(): Promise<ResultObject<string>> {
    return request.get({
        url: "/about"
    })
}