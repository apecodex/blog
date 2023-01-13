declare type TagEntity = {
    id: string
    name: string
}

declare type TagSearchEntity = {
    id: string
    name: string
}

declare type ArticleTagEntity = TagSearchEntity

// 标签列表
declare type TagFrontEntity = {
    tags: Array<TagEntity>
    newsArticleList: Array<NewsArticleEntity>
    newsCommentList: Array<NewCommentEntity>
}
