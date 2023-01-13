declare type PictureEntity = {
    pictureName: string
    pictureSrc: string
    width: number | null
    height: number | null
    size: string
    createTime: string
}

// 图片相册信息
declare type PictureAlbumInfoEntity = {
    albumName: string
    albumDesc: string
    createTime: string
    updateTime: string
}