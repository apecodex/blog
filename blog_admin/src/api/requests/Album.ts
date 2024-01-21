import request from '~/api/service'

/**
 * 获取后台列表相册
 * @param condition
 */
export function listAlbumBack(condition: ConditionParams): Promise<PageResult<Array<AlbumBackModel>>> {
  return request.get({
    url: '/admin/albums',
    params: condition,
    isDecrypt: true
  })
}

/**
 * 上传相册封面
 * @param data
 */
export function updateAlbumCover(data: any): Promise<ResultObject<UploadFileInfoModel>> {
  return request.post({
    url: '/admin/album/cover',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

/**
 * 修改相册信息
 * @param data
 */
export function updateAlbum(data: PhotoAlbumParams): Promise<ResultObject<null>> {
  return request.put({
    url: '/admin/album',
    data,
    isEncrypt: true
  })
}

/**
 * 上删除相册
 * @param albumId
 */
export function deleteAlbum(albumId: string): Promise<ResultObject<null>> {
  return request.delete({
    url: '/admin/picture/album/' + albumId,
  })
}

/**
 * 新增相册
 * @param data
 */
export function saveAlbum(data: PhotoAlbumParams): Promise<ResultObject<null>> {
  return request.post({
    url: '/admin/album',
    data,
    isEncrypt: true
  })
}