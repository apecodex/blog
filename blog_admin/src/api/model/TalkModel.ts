declare type TalkPictureVideoModel = {
    id: string,
    src: string,
    fileName: string
}

declare type TalkBackOnlyModel = {
    id: string,
    content: string,
    isTop: boolean,
    status: number,
    pictureVideos: Array<TalkPictureVideoModel>
}

declare type TalkBackModel = {
    user: SimpleUserInfoModel,
    id: string,
    content: string,
    isTop: boolean,
    status: number,
    pictureVideos: Array<TalkPictureVideoModel>,
    likeCount: number,
    commentCount: number,
    createTime: string,
    updateTime: string,
}