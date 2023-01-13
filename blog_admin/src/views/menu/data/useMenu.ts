import type {DataTableColumns, SelectOption} from 'naive-ui'
import type {Ref} from 'vue'
import {computed, h, reactive, ref} from 'vue'
import {NButton, NIcon, NSpace, NTag, NText} from "naive-ui";
import {isMobile} from "~/utils/utils";
import iconMap from '~/utils/icons'
import {listMenuBack, saveOrUpdateMenu, deleteMenu} from '~/api/requests/Menu'
import {StatusCode} from "~/api/enum/statusCode";

const menuData: Ref<Array<MenuBackModel>> = ref<Array<MenuBackModel>>([])
const loading = ref(false)
const showSaveOrUpdateModel = ref(false)
const menuForm: MenuParams = reactive({
    id: null,
    name: '',
    title: '',
    path: '',
    component: '',
    icon: '',
    parentId: null,
    orderNum: 1,
    isEnable: false,
})
const menuParentOptions: Ref<Array<SelectOption>> = ref<Array<MenuBackModel>>([])
// 点击修改时保存当前点击的菜单，用于判断父菜单是否被修改
const currentMenu: Ref<MenuBackModel | null> = ref<MenuBackModel | null>(null)

const createColumns = ({menu}: { menu: (row: MenuBackModel) => void }): DataTableColumns<MenuBackModel> => {
    return [
        {
            title: '标题',
            key: 'title',
            width: 150,
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const type = row.parentId ? 'info' : 'success'
                return h(NText, {type}, {
                    default: () => row.title
                })
            }
        },
        {
            title: '标识',
            key: 'name',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: "3"}, {
                    default: () => row.name
                })
            }
        },
        {
            title: '路由',
            key: 'path',
            width: 150,
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {italic: true, depth: "2"}, {
                    default: () => row.path
                })
            }
        },
        {
            title: '组件',
            key: 'component',
            width: 160,
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {italic: true, type: 'success'}, {
                    default: () => row.component
                })
            }
        },
        {
            title: '图标',
            key: 'icon',
            width: 60,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const icon = iconMap.get(row.icon)
                return icon === undefined ? h(NText, {depth: 3}, {
                    default: () => row.icon
                }) : h(NIcon, {size: 20, color: '#ffffff'}, {
                    default: () => h(icon as any)
                })
            }
        },
        {
            title: '排序',
            key: 'orderNum',
            width: 50,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const type = row.parentId ? 'info' : 'warning'
                return h(NText, {type}, {
                    default: () => row.orderNum
                })
            }
        },
        {
            title: '状态',
            key: 'isEnable',
            width: 80,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const type = row.isEnable ? 'success' : 'error'
                return h(NTag, {type}, {
                    default: () => row.isEnable ? '正常' : '禁用'
                })
            }
        },
        {
            title: '创建时间',
            key: 'createTime',
            width: 180,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: '3'}, {
                    default: () => row.createTime
                })
            }
        },
        {
            title: '操作',
            key: 'options',
            width: 130,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(NSpace, {justify: 'center'}, {
                    default: () => [
                        h(NButton, {
                            size: 'small',
                            tertiary: true,
                            type: 'info',
                            onClick: () => {
                                currentMenu.value = row
                                menuForm.id = row.id
                                menuForm.name = row.name
                                menuForm.title = row.title
                                menuForm.path = row.path
                                menuForm.component = row.component
                                menuForm.icon = row.icon
                                menuForm.parentId = row.parentId
                                menuForm.orderNum = String(row.orderNum)  // 装为String类型，不然会报错，因为NInput不支持number类型
                                menuForm.isEnable = row.isEnable
                                menuParentOptions.value.length = 0
                                const parentMenu = menuData.value.filter((menu: MenuBackModel) => menu.parentId === null && menu.id !== row.id).map((parent) => {
                                    return {label: parent.title, value: parent.id}
                                });
                                menuParentOptions.value = Array.from(parentMenu)
                                showSaveOrUpdateModel.value = true
                            }
                        }, {
                            default: () => '修改'
                        }),
                        h(NButton, {
                            size: 'small',
                            tertiary: true,
                            type: 'error',
                            onClick: () => {
                                (<any>window).$dialog.warning({
                                    title: '警告',
                                    content: `你确定删除'${row.title}'吗？`,
                                    positiveText: '确定',
                                    negativeText: '算了',
                                    onPositiveClick: async () => {
                                        let tipMessage = (<any>window).$message.create('保存中...', {
                                            type: 'loading'
                                        })
                                        await deleteMenu(row.id).then((resp: ResultObject<null>) => {
                                            if (resp.status) {
                                                menuData.value.length = 0
                                                createMenuData()
                                                tipMessage.type = 'success'
                                                tipMessage.content = resp.message
                                            }
                                            if (resp.code === StatusCode.FAIL) {
                                                tipMessage.type = 'warning'
                                                tipMessage.content = resp.message
                                            }
                                        }).catch(() => {
                                            tipMessage.type = 'error'
                                            tipMessage.content = '删除失败'
                                        })
                                    },
                                })
                            }
                        }, {
                            default: () => '删除'
                        }),
                    ]
                })
            }
        }
    ]
}

const columns = createColumns({
    menu(row: MenuBackModel) {
    }
})

const createMenuData = async () => {
    loading.value = true
    menuData.value.length = 0
    await listMenuBack().then((resp: ResultObject<Array<MenuBackModel>>) => {
        if (resp.status) {
            menuData.value = Array.from(resp.data)
        }
    }).catch(() => {
        (<any>window).$message.error("获取菜单列表失败，请重试")
    }).finally(() => {
        loading.value = false
    })
}

// 打开新增的模态框
const showSaveOrUpdateModelHandle = () => {
    menuForm.id = null
    menuForm.name = ''
    menuForm.title = ''
    menuForm.path = ''
    menuForm.component = ''
    menuForm.icon = ''
    menuForm.parentId = null
    menuForm.orderNum = 1
    menuForm.isEnable = false
    menuParentOptions.value.length = 0
    const parentMenu = menuData.value.filter((menu: MenuBackModel) => menu.parentId === null).map((parent) => {
        return {label: parent.title, value: parent.id}
    });
    menuParentOptions.value = Array.from(parentMenu)
    showSaveOrUpdateModel.value = true
}

const checkMenuFormComputed = computed(() => {
    const title = menuForm.title.length !== 0
    const name = menuForm.name.length !== 0
    const path = menuForm.path.length !== 0
    const component = menuForm.component.length !== 0
    const icon = menuForm.icon.length !== 0
    return title && name && path && component && icon
})

// 新增或修改时更新菜单排序
const changeOrderNum = (isAdd: boolean) => {
    if (isAdd) {
        // 获取所有父菜单，且拿到最后一位菜单的排序值+1
        const parentMenu = menuData.value.filter((menu: MenuBackModel) => menu.parentId === null)
        const maxOrderNum = parentMenu[parentMenu.length - 1].orderNum
        menuForm.orderNum = String(maxOrderNum + 1)
    } else {
        // 通过父菜单id获取要插入的父菜单，且拿到父菜单的子菜单最后一位菜单的排序值+1
        const parentMenu = menuData.value.filter((menu: MenuBackModel) => menu.id === menuForm.parentId)
        if (parentMenu.length !== 0) {
            if (parentMenu[0].children !== null) {
                const children = parentMenu[0].children as Array<MenuBackModel>
                const maxOrderNum = children[children.length - 1].orderNum
                menuForm.orderNum = String(maxOrderNum + 1)
            } else menuForm.orderNum = '1'
        }
    }
}

// 保存菜单
const saveOrUpdateHandle = async () => {
    if (!checkMenuFormComputed) {
        return;
    }
    // 修改菜单时
    if (menuForm.id !== null) {
        // 有父菜单,且父菜单与原先的不同时
        if (menuForm.parentId !== null && menuForm.parentId !== currentMenu.value?.parentId) {
            changeOrderNum(false)
        }
    } else { // 新增菜单时
        // 有父菜单
        if (menuForm.parentId !== null) {
            changeOrderNum(false)
        } else {
            changeOrderNum(true)
        }
    }
    const form = new FormData()
    form.append('component', menuForm.component)
    form.append('isEnable', menuForm.isEnable as any)
    form.append('icon', menuForm.icon)
    form.append('id', menuForm.id ? menuForm.id : '')
    form.append('name', menuForm.name)
    form.append('orderNum', menuForm.orderNum as any)
    form.append('parentId', menuForm.parentId ? menuForm.parentId : '')
    form.append('path', menuForm.path)
    form.append('title', menuForm.title)
    let tipMessage = (<any>window).$message.create('保存中...', {
        type: 'loading'
    })
    await saveOrUpdateMenu(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            showSaveOrUpdateModel.value = false
            menuData.value.length = 0
            createMenuData()
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '保存失败'
    })
}

export {
    menuData,
    columns,
    createMenuData,
    loading,
    showSaveOrUpdateModel,
    showSaveOrUpdateModelHandle,
    menuForm,
    menuParentOptions,
    saveOrUpdateHandle,
    checkMenuFormComputed,
}