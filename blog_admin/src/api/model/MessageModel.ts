declare type MessageBackModel = {
    id: string
    user: SimpleUserInfoModel | null
    nickname: string
    avatar: string
    content: string
    theme: string
    isReview: boolean
    ipAddress: string
    ipSource: string
    createTime: string
}