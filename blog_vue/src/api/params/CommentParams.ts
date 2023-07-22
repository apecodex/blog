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

/**
 * 发送评论
 */
declare type SaveCommentParams = {
  topicId: string
  commentContent: string
  parentId?: string
  replyCommentId?: string
  type: number
}
