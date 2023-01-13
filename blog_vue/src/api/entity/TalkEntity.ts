declare type TalkEntity = {
    user: SimpleUserInfoEntity
    id: string
    content: string
    isTop: boolean
    src: Array<string>
    likeCount: number | null
    commentCount: number | null
    createTime: string | Date
    rectangle: string | null
    isLike: Boolean
}