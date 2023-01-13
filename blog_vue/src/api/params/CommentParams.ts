declare type commentParams = {
    commentContent: string
    parentId?: string
    replyCommentId?: string
    topicId: string
    type: number
}

// 查询评论参数
declare type CommentQueryParams = {
    topicId: string,
    type: number,
    current?: number,
    size?: number
}
