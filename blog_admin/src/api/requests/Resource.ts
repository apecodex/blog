import request from '~/api/service'

/**
 * 获取角色资源选项
 */
export function listRoleResourceOption(): Promise<ResultObject<Array<ResourceOrMenuOptionModel>>> {
  return request.get({
    url: '/admin/role/resources',
    isDecrypt: true
  })
}

/**
 * 获取后台资源列表
 * @param condition
 */
export function listResourceBack(condition: ConditionParams): Promise<ResultObject<Array<ResourceBackModel>>> {
  return request.get({
    url: '/admin/resources',
    params: condition,
    isDecrypt: true
  })
}

/**
 * 新增或修改资源
 * @param data
 */
export function saveOrUpdateResource(data: ResourceParams): Promise<ResultObject<null>> {
  return request.post({
    url: '/admin/resource',
    data,
    isEncrypt: true
  })
}

/**
 * 删除资源
 * @param resourceId
 */
export function deleteResource(resourceId: string): Promise<ResultObject<null>> {
  return request.delete({
    url: '/admin/resource/' + resourceId,
  })
}