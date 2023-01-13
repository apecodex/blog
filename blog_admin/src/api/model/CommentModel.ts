declare type CommentBackModel  = {
    id: string,
    user: SimpleUserInfoModel,
    replyUser: null | SimpleUserInfoModel
    commentContent: string
    articleTitle: null | string
    replyCount: number | null
    likeCount: number | null
    type: string
    ipSource: string
    browser: string
    os: string
    isReview: boolean
    createTime: string
}