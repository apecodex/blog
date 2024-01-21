import request from '~/api/service'

/**
 * 后台评论列表
 * @param condition
 */
export function listCommentBack(condition: ConditionParams): Promise<PageResult<Array<CommentBackModel>>> {
    return request.get({
        url: '/admin/comments',
        params: condition,
        isDecrypt: true
    })
}

/**
 * 修改评论审核
 * @param data
 */
export function updateCommentReview(data: { idList: Array<string>, isReview: boolean }): Promise<ResultObject<null>> {
    return request.put({
        url: '/admin/comment',
        data
    })
}

/**
 * 删除评论
 * @param data
 */
export function deleteComment(data: FormData): Promise<ResultObject<null>> {
    return request.delete({
        url: '/admin/comment',
        data
    })
}