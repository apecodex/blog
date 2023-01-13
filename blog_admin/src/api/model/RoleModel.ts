// 角色选项
declare type roleOptionModel = {
    id: string,
    roleDesc: string
}

// 角色后台
declare type RoleBackModel = {
    id: string
    roleAuth: string
    roleDesc: string
    createTime: string
    isEnable: boolean
    resourceIdList: Array<string>
    menuIdList: Array<string>
}

// 资源或菜单选项
declare type ResourceOrMenuOptionModel = {
    "id": string,
    "label": string,
    "children": Array<ResourceOrMenuOptionModel>
}