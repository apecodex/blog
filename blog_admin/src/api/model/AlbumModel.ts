declare type AlbumBackModel  = {
    id: string,
    user: SimpleUserInfoModel | null,
    albumName: string,
    albumDesc: string,
    albumCover: string,
    pictureCount: number,
    status: boolean,
    createTime: string,
    updateTime: string
}