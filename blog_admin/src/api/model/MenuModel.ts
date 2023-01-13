/**
 * 当前用户菜单
 */
declare type UserMenuBackModel = {
  id: string,
  name: string,
  title: string,
  path: string,
  component: string,
  icon: string,
  parentId: string | null,
  orderNum: number,
  isEnable: boolean,
  createTime: string,
  children: UserMenuBackModel[] | null
}

/**
 * 后台菜单
 */
declare type MenuBackModel = UserMenuBackModel