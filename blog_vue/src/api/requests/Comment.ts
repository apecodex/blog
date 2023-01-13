import {request} from "@/api/services"

/**
 * 插入评论
 * @param data
 */
export function insertComment(data: FormData): Promise<ResultObject<null>> {
    return request.post({
        url: "/comment",
        data
    })
}

/**
 * 获取评论
 * @param commentInfo
 */
export function getCommentList(commentInfo: CommentQueryParams): Promise<PageResult<Array<CommentEntity>>> {
    return request.get({
        url: "/comments",
        params: commentInfo
    })
}

/**
 * 查询评论下的回复
 * @param commentId
 * @param condition
 */
export function getReplyCommentList(commentId: string, {size, current}: ConditionParams): Promise<ResultObject<Array<ReplyEntity>>> {
    return request.get({
        url: `/comment/${commentId}/replies`,
        params: {size,current}
    })
}

/**
 * 评论点赞
 * @param commentId
 */
export function commentLike(commentId: string): Promise<ResultObject<null>> {
    return request.post({
        url: `/comment/${commentId}/like`
    })
}