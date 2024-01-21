import {request} from '@/api/services'

/**
 * 获取首页文章
 * @param articleId
 */
export function getHomeArticleList(condition: ConditionParams): Promise<PageResult<Array<ArticleHomeEntity>>> {
    return request.get({
        url: "/articles",
        params: condition,
        isDecrypt: true
    })
}

/**
 * 根据文章id获取文章
 * @param articleId
 */
export function getArticleByArticleId(articleId: string): Promise<ResultObject<ArticleEntity>> {
    return request.get({
        url: '/article/' + articleId,
        isDecrypt: true
    })
}

/**
 * 根据条件搜索文章
 * @param condition
 */
export function getArticlesByCondition(condition: ConditionParams): Promise<PageResultWithObject<Array<ArticleSearchConditionEntity>, string>> {
    return request.get({
        url: "/article/search/condition",
        params: condition
    })
}

/**
 * 文章点赞
 * @param articleId
 */
export function articleLike(articleId: string): Promise<ResultObject<null>> {
    return request.post({
        url: `/articles/${articleId}/like`
    })
}

/**
 * 获取文章归档
 */
export function getArticleArchives(): Promise<ResultObject<Array<ArchiveEntity>>> {
    return request.get({
        url: "/article/archives",
        isDecrypt: true
    })
}

/**
 * 搜索
 * @param keywords
 */
export function search({keywords}: ConditionParams): Promise<ResultObject<SearchEntity>> {
    return request.get({
        url: "/search",
        params: {keywords}
    })
}