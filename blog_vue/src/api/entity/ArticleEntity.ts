declare type ArticleHomeEntity = {
    id: string
    articleTitle: string
    articleCover: string,
    articleContent: string,
    categoryId: string,
    categoryName: string,
    likeCount: number | null,
    viewsCount: number | null,
    type: number | string,
    tags: Array<ArticleTagEntity>,
    isTop: boolean,
    createTime: string
}

// 上一篇文章
declare type LastArticleEntity = {
    id: string,
    articleTitle: string,
    articleCover: string
}

// 下一篇文章
declare type NextArticleEntity = LastArticleEntity

// 推荐文章
declare type RecommendArticleEntity = {
    id: string,
    articleTitle: string,
    articleCover: string,
    createTime: string
}

// 新文章
declare type NewsArticleEntity = RecommendArticleEntity

declare type ArticleEntity = {
    author: SimpleUserInfoEntity
    id: string
    articleTitle: string
    articleCover: string
    articleContent: string
    categoryId: string
    categoryName: string
    likeCount: number | null
    viewsCount: number | null
    tags: Array<ArticleTagEntity>
    type: string
    originalUrl: string | null
    createTime: string
    updateTime: string
    lastArticle: LastArticleEntity
    nextArticle: NextArticleEntity
    recommendArticleList: Array<RecommendArticleEntity>
    newsArticleList: Array<NewsArticleEntity>
    newsCommentList: Array<NewCommentEntity>
    rectangle: string | null
}

// 根据条件搜索的文章
declare type ArticleSearchConditionEntity = {
    articleId: string,
    articleTitle: string,
    articleCover: string,
    type: string,
    createTime: string,
    categoryId: string,
    categoryName: string,
    tags: Array<ArticleTagEntity>
}

// 文章归档
declare type ArchiveEntity = {
    month: string;
    articles: Array<{
        id: string
        articleTitle: string
        createTime: string
    }>
}

declare type ArticleSearchEntity = {
    id: string,
    articleTitle: string
    articleContent: string
}

// 文章搜索
declare type SearchEntity = {
    count: number | null
    articleSearch: Array<ArticleSearchEntity>
    categorySearch: Array<CategorySearchEntity>
    tagSearch: Array<TagSearchEntity>
}