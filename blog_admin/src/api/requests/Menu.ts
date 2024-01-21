import request from '../service'

/**
 * 获取当前用户菜单
 */
export function getUserMenus(): Promise<ResultObject<Array<UserMenuBackModel>>> {
  return request.get({
    url: "/admin/user/menus",
    isDecrypt: true
  })
}

/**
 * 获取角色菜单选项
 */
export function listRoleMenuOption(): Promise<ResultObject<Array<ResourceOrMenuOptionModel>>> {
  return request.get({
    url: '/admin/role/menus',
    isDecrypt: true
  })
}

/**
 * 获取后台菜单
 */
export function listMenuBack(): Promise<ResultObject<Array<MenuBackModel>>> {
  return request.get({
    url: '/admin/menus',
    isDecrypt: true
  })
}

/**
 * 新增或修改菜单
 * @param data
 */
export function saveOrUpdateMenu(data: MenuParams): Promise<ResultObject<null>> {
  return request.post({
    url: '/admin/menu',
    data,
    isEncrypt: true
  })
}

/**
 * 删除菜单
 * @param menuId
 */
export function deleteMenu(menuId: string): Promise<ResultObject<null>> {
  return request.delete({
    url: '/admin/menu/' + menuId
  })
}