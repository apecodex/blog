declare type ResourceBackModel = {
    id: string,
    name: string,
    url: string | null,
    requestMethod: string | null,
    isEnable: boolean,
    parentId: string | null,
    isAnonymous: boolean,
    createTime: string,
    updateTime: string,
    children: Array<ResourceBackModel> | null
}