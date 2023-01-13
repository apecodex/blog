declare type MenuParams = {
    id: string | null,
    name: string,
    title: string,
    path: string,
    component: string,
    icon: string,
    parentId: string | null,
    orderNum: number | string,
    isEnable: boolean,
}