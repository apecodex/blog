declare type SimpleUserInfoEntity = {
    "uid": string,
    "nickname": string,
    "avatar": string,
    "intro": string,
    "webSite": string
}

declare type UserEntity = {
    nickname: string
    uid: string
    email: string | null
    avatar: string
    articleLikeSet: Array<string>
    commentLikeSet: Array<string>
    talkLikeSet: Array<string>
    token: string
    tokenHead: string
}

// 上传的文件信息
declare type UploadFileInfoModel = {
    width: number
    height: number
    size: string
    url: string
}

// 用户信息
declare type UserInfoModel = {
    email: string | null
    uid: string
    loginType: 0
    nickname: string
    avatar: string
    intro: string
    webSite: string
    isEmailNotice: boolean
    bindQQ: boolean
    createTime: string
}