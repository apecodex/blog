declare type CategoryEntity = {
    id: string,
    name: string,
    articleCount: number
}

// 分类列表
declare type CategoryFrontEntity = {
    categories: CategoryEntity
    newsArticleList: NewsArticleEntity
    newsCommentList: NewCommentEntity
}

// 分类搜索
declare type CategorySearchEntity = {
    id: string,
    name: string,
}