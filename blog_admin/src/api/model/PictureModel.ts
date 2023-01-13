declare type PictureBackModel = {
    id: string,
    pictureName: string,
    pictureSrc: string,
    width: number | null
    height: number | null
    size: string | null
    isDelete: boolean,
    user: SimpleUserInfoModel | null,
    createTime: string,
    updateTime: string
}

// 上传的文件信息
declare type UploadFileInfoModel = {
    width: number
    height: number
    size: string
    url: string
}