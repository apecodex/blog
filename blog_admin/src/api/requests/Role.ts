import request from "~/api/service";

/**
 * 获取角色选项
 */
export function getRoleOptions(): Promise<ResultObject<Array<roleOptionModel>>> {
  return request.get({
    url: '/admin/user/roles',
    isDecrypt: true
  })
}

/**
 * 获取角色列表
 * @param condition
 */
export function listRole(condition: ConditionParams): Promise<PageResult<Array<RoleBackModel>>> {
  return request.get({
    url: '/admin/roles',
    params: condition,
    isDecrypt: true
  })
}

/**
 * 保存或修改角色
 * @param data
 */
export function saveOrUpdateRole(data: RoleParams): Promise<ResultObject<null>> {
  return request.post({
    url: '/admin/role',
    data,
    isEncrypt: true
  })
}

/**
 * 删除角色
 * @param data
 */
export function deleteRole(data: string): Promise<ResultObject<null>> {
  return request.delete({
    url: '/admin/role',
    data
  })
}