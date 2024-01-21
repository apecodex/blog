import requests from '../service'

/**
 * 上传文章封面
 * @param file 
 * @returns 
 */
export function uploadArticleCover(file: any): Promise<ResultObject<UploadFileInfoModel>> {
  return requests.post({
    url: '/admin/article/cover',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: file
  })
}

/**
 * 删除文章封面
 * @param filePath 
 * @returns 
 */
export function removeArticleCover(filePath: string): Promise<ResultObject<null>> {
  return requests.delete({
    url: '/admin/pictures/delete',
    data: [filePath]
  })
}

/**
 * 保存或更新文章
 * @param articleParams 
 * @returns 
 */
export function saveOrUpdateArticle(articleParams: SaveOrUpdateArticleParams): Promise<ResultObject<null>> {
  return requests.post({
    url: '/admin/article',
    data: articleParams,
    isEncrypt: true
  })
}

/**
 * 通过文章id获取后台文章
 * @param articleId
 * @returns 
 */
export function getArticleBackByArticleId(articleId: string): Promise<ResultObject<OnlyArticleBackModel>> {
  return requests.get({
    url: "/admin/article/" + articleId,
    isDecrypt: true
  })
}

/**
 * 修改文章顶置
 * @param data
 * @returns 
 */
export function updateArticleTop(data: FormData): Promise<ResultObject<null>> {
  return requests.put({
    url: "/admin/article/top",
    data
  })
}

/**
 * 后台文章列表
 * @param conditon
 */
export function listArticleBack(conditon: ConditionParams): Promise<PageResult<Array<ArticleBackModel>>> {
  return requests.get({
    url: "/admin/articles",
    params: conditon,
    isDecrypt: true
  })
}

/**
 * 更新文章删除状态
 * @param data
 */
export function updateArticleDeleteStatus(data: {idList: Array<string>, isDelete: boolean}): Promise<ResultObject<null>> {
  return requests.put({
    url: '/admin/article',
    data
  })
}

/**
 * 删除文章
 * @param idList
 */
export function deleteArticle(idList: Array<string>): Promise<ResultObject<null>> {
  return requests.delete({
    url: '/admin/article',
    data: idList
  })
}