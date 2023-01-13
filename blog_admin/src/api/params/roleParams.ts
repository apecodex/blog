// 保存或修改角色
declare type RoleParams = {
    isEnable: boolean
    menuIdList?: Array<string> | null
    resourceIdList?: Array<string> | null
    roleAuth: string
    roleDesc: string
    roleId?: string
}