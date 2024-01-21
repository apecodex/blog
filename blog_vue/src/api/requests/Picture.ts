import {request} from "@/api/services"

/**
 * 根据相册id获取图片
 * @param albumId
 */
export function getPicturesByAlbumId(condition: ConditionParams): Promise<PageResultWithObject<Array<PictureEntity>, PictureAlbumInfoEntity>> {
    return request.get({
        url: `/album/${condition.albumPath}/pictures`,
        params: condition,
        isDecrypt: true
    })
}