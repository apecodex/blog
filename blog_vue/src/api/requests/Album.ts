import {request} from "@/api/services"

/**
 * 获取相册列表
 * @param condition
 */
export function getAlbums(condition: ConditionParams): Promise<PageResult<Array<AlbumEntity>>> {
    return request.get({
        url: "/albums",
        params: condition,
        isDecrypt: true
    })
}