// 新增或修改资源参数
declare type ResourceParams = {
    id: string | null
    isAnonymous: boolean
    isEnable: boolean
    name: string
    parentId: string | null
    requestMethod: string | null
    url: string | null
}