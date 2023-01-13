// 最新评论
declare type NewCommentEntity = {
    topicId: string
    type: number
    user: SimpleUserInfoEntity
    commentContent: string
    createTime: string
}

declare type CommentEntity = {
    id: string
    user: SimpleUserInfoEntity
    commentContent: string
    distance: null | string
    browser: string
    os: string
    replyCount: number | null
    likeCount: number | null
    createTime: string
    replyList: Array<ReplyEntity>
}


declare type ReplyEntity = {
    id: string
    parentId: string
    replyCommentId: string
    user: SimpleUserInfoEntity
    replyUser: SimpleUserInfoEntity | null
    commentContent: string
    distance: null | string
    browser: string
    os: string
    likeCount: null | number
    createTime: string
}