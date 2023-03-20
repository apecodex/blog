declare type TagEntity = {
    id: string
    name: string
}

declare type TagSearchEntity = {
    id: string
    name: string
}

declare type ArticleTagEntity = TagSearchEntity

declare type TagWithArticleCount = {
    id: string
    name: string,
    articleCount: number
}

// 标签列表
declare type TagFrontEntity = {
    tags: Array<TagWithArticleCount>
    newsArticleList: Array<NewsArticleEntity>
    newsCommentList: Array<NewCommentEntity>
}
