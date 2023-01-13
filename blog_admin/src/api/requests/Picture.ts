import request from '~/api/service'

/**
 * 通过相册id获取图片列表
 * @param condition
 */
export function listPictureByAlbumId(condition: ConditionParams): Promise<PageResult<Array<PictureBackModel>>> {
    return request.get({
        url: '/admin/pictures',
        params: condition
    })
}

/**
 * 更新图片删除状态
 * @param data
 */
export function updatePictureDeleteStatus(data: {idList: Array<string>, isDelete: boolean}): Promise<ResultObject<null>> {
    return request.put({
        url: '/admin/pictures/delete',
        data
    })
}

/**
 * 移动图片相册
 * @param data
 */
export function movePictureAlbum(data: {albumId: string, pictureIdList: Array<string>}): Promise<PageResult<null>> {
    return request.put({
        url: '/admin/pictures/album',
        data
    })
}

/**
 * 修改图片信息
 * @param data
 */
export function updatePictureInfo(data: FormData): Promise<ResultObject<null>> {
    return request.put({
        url: '/admin/picture',
        data
    })
}

/**
 * 上传图片
 * @param data
 */
export function uploadPicture(data: any): Promise<ResultObject<UploadFileInfoModel>> {
    return request.post({
        url: '/admin/picture/upload',
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        data
    })
}

/**
 * 保存图片到相册
 * @param data
 */
export function savePictures(data: {albumId: string, pictureUrlList: Array<UploadFileInfoModel>}): Promise<ResultObject<null>> {
    return request.post({
        url: '/admin/picture',
        data
    })
}

/**
 * 获取回收站
 * @param condition
 */
export function listDeletePicture(condition: ConditionParams): Promise<PageResult<Array<PictureBackModel>>> {
    return request.get({
        url: '/admin/pictures/delete',
        params: condition
    })
}

/**
 * 删除图片
 * @param data
 */
export function deletePicture(data: Array<string>): Promise<ResultObject<null>> {
    return request.delete({
        url: '/admin/picture',
        data
    })
}