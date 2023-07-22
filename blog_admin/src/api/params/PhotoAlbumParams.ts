/**
 * 添加或修改相册
 */
declare type PhotoAlbumParams = {
  albumId?: string
  albumName: string
  albumDesc: string
  albumCover: string
  status: boolean
  isDelete?: boolean
}