import request from '../service'

/**
 * 搜索文章分类
 * @param categoryName 
 * @returns 
 */
export function searchArticleCategory(categoryName?: string): Promise<ResultObject<Array<CategorySearchModel>>> {
  return request.get({
    url: "/admin/category/search",
    params: { keywords: categoryName }
  })
}

/**
 * 保存或修改分类
 * @param data
 */
export function saveOrUpdateCategory(data: FormData): Promise<ResultObject<null>> {
  return request.post({
    url: '/admin/category',
    data
  })
}

/**
 * 获取后台分类
 * @param condition
 */
export function listCategoryBack(condition: ConditionParams): Promise<PageResult<Array<CategoryBackModel>>> {
  return request.get({
    url: '/admin/categories',
    params: condition
  })
}

/**
 * 删除分类
 * @param data
 */
export function deleteCategory(data: FormData): Promise<ResultObject<null>> {
  return request.delete({
    url: '/admin/category',
    data
  })
}