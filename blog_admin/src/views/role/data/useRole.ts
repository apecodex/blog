import type {Ref} from 'vue'
import {listRole, saveOrUpdateRole, deleteRole} from '~/api/requests/Role'
import {listRoleResourceOption} from '~/api/requests/Resource'
import {listRoleMenuOption} from '~/api/requests/Menu'
import {StatusCode} from "~/api/enum/statusCode";
import {computed, reactive, ref} from "vue";

// 角色数据
const roleData: Ref<Array<RoleBackModel>> = ref<Array<RoleBackModel>>([])
const defaultTabValue = ref("")
// 角色资源选项数据
const roleResourceData: Ref<Array<ResourceOrMenuOptionModel>> = ref<Array<ResourceOrMenuOptionModel>>([])
// 角色菜单选项数据
const roleMenuData: Ref<Array<ResourceOrMenuOptionModel>> = ref<Array<ResourceOrMenuOptionModel>>([])
// 已选资源选项
const checkedResources: Ref<Array<string>> = ref<Array<string>>([])
// 已选菜单选项
const checkedMenus: Ref<Array<string>> = ref<Array<string>>([])
// 默认新增表单
const defaultRoleInfoForm: { roleAuth: string, roleDesc: string, isEnable: boolean } = reactive({
    roleDesc: '',
    roleAuth: '',
    isEnable: false
})
// 显示修改角色模态框
const showRoleInfoModal = ref(false)
// 显示新增角色模态框
const showSaveNewRoleModal = ref(false)
// 修改角色选项表单
const roleInfoForm: Ref<RoleParams | null> = ref<RoleParams | null>(null)

const createRoleData = async (condition: ConditionParams) => {
    roleData.value.length = 0
    await listRole(condition).then((resp: PageResult<Array<RoleBackModel>>) => {
        if (resp.status) {
            roleData.value = Array.from(resp.data.recordList)
            if (roleData.value.length !== 0) {
                defaultTabValue.value = roleData.value[0].id
            }
        }
    }).catch(() => {
        (<any>window).$message.error("获取角色列表失败，请重试");
    }).finally(() => {
        // 首次打开，初始化第一个标签的数据
        if (roleData.value.length !== 0) {
            checkedResources.value.length = 0
            checkedMenus.value.length = 0
            checkedResources.value = Array.from(roleData.value[0].resourceIdList)
            checkedMenus.value = Array.from(roleData.value[0].menuIdList)
        }
    })
}

const createRoleResourceData = async () => {
    roleResourceData.value.length = 0
    await listRoleResourceOption().then((resp: ResultObject<Array<ResourceOrMenuOptionModel>>) => {
        if (resp.status) {
            roleResourceData.value = Array.from(resp.data)
        }
    }).catch(() => {
        (<any>window).$message.error("获取角色资源列表失败，请重试");
    })
}

const createRoleMenuData = async () => {
    roleMenuData.value.length = 0
    await listRoleMenuOption().then((resp: ResultObject<Array<ResourceOrMenuOptionModel>>) => {
        if (resp.status) {
            roleMenuData.value = Array.from(resp.data)
        }
    }).catch(() => {
        (<any>window).$message.error("获取角色菜单列表失败，请重试");
    })
}

// 切换Tab时进行重新赋值已选资源选项和菜单选项
const changeTabHandle = (value: string | number) => {
    roleData.value.map((role: RoleBackModel) => {
        if (role.id === value) {
            checkedResources.value.length = 0
            checkedMenus.value.length = 0
            checkedResources.value = Array.from(role.resourceIdList)
            checkedMenus.value = Array.from(role.menuIdList)
        }
    })
}

// 资源选项勾选时
const updateResourceCheckedKeys = (keys: Array<string | number>) => {
    checkedResources.value.length = 0
    checkedResources.value = Array.from(keys) as Array<string>
}

// 菜单选项勾选时
const updateMenuCheckedKeys = (keys: Array<string | number>) => {
    checkedMenus.value.length = 0
    checkedMenus.value = Array.from(keys) as Array<string>
}

// 更新角色资源
const updateResourceHandle = async (role: RoleBackModel) => {
    let tipMessage = (window as any).$message.create("正在更新权限...", {
        type: 'loading'
    });
    await saveOrUpdateRole({
        isEnable: role.isEnable,
        menuIdList: role.menuIdList,
        resourceIdList: checkedResources.value,
        roleAuth: role.roleAuth,
        roleDesc: role.roleDesc,
        roleId: role.id
    }).then((resp: ResultObject<null>) => {
        if (resp.status) {
            role.resourceIdList.length = 0
            role.resourceIdList = Array.from(checkedResources.value)
            tipMessage.type = 'success'
            tipMessage.content = `${role.roleDesc} 的权限更新成功`
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '权限更新失败'
    })
}

// 更新角色菜单
const updateMenuHandle = async (role: RoleBackModel) => {
    let tipMessage = (window as any).$message.create("正在更新菜单...", {
        type: 'loading'
    });
    await saveOrUpdateRole({
        isEnable: role.isEnable,
        menuIdList: checkedMenus.value,
        resourceIdList: role.resourceIdList,
        roleAuth: role.roleAuth,
        roleDesc: role.roleDesc,
        roleId: role.id
    }).then((resp: ResultObject<null>) => {
        if (resp.status) {
            role.menuIdList.length = 0
            role.menuIdList = Array.from(checkedMenus.value)
            tipMessage.type = 'success'
            tipMessage.content = `${role.roleDesc} 的菜单更新成功，重新登录后生效`
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '菜单更新失败'
    })
}


// 更新修改的角色信息
const updateRoleInfoHandle = async () => {
    let tipMessage = (window as any).$message.create("正在修改...", {
        type: 'loading'
    });
    let flag = true
    if (roleInfoForm.value?.roleDesc.length === 0) {
        roleInfoForm.value.roleDesc = defaultRoleInfoForm.roleDesc;
        flag = false;
        (<any>window).$message.warning("角色名称不能为空");
    }
    if (roleInfoForm.value?.roleAuth.length === 0) {
        roleInfoForm.value.roleAuth = defaultRoleInfoForm.roleAuth;
        flag = false;
        (<any>window).$message.warning("角色权限名不能为空");
    }
    if (flag) {
        await saveOrUpdateRole(roleInfoForm.value as RoleParams).then((resp: ResultObject<null>) => {
            if (resp.status) {
                roleData.value.map((role: RoleBackModel) => {
                    if (role.id === roleInfoForm.value?.roleId) {
                        role.roleDesc = roleInfoForm.value?.roleDesc
                        role.roleAuth = roleInfoForm.value?.roleAuth
                        role.isEnable = roleInfoForm.value?.isEnable
                    }
                })
                showRoleInfoModal.value = false
                tipMessage.type = 'success'
                tipMessage.content = resp.message
            }
            if (resp.code === StatusCode.FAIL) {
                tipMessage.type = 'warning'
                tipMessage.content = resp.message
            }
        }).catch(() => {
            tipMessage.type = 'error'
            tipMessage.content = '修改失败'
        })
    } else {
        tipMessage.type = 'warning'
        tipMessage.content = '修改失败'
    }
}

//  新增角色按钮处理
const saveNewRoleHandle = async () => {
    let tipMessage = (window as any).$message.create("保存中...", {
        type: 'loading'
    });
    let flag = true
    if (roleInfoForm.value?.roleDesc.length === 0) {
        roleInfoForm.value.roleDesc = defaultRoleInfoForm.roleDesc;
        flag = false;
        (<any>window).$message.warning("角色名称不能为空");
    }
    if (roleInfoForm.value?.roleAuth.length === 0) {
        roleInfoForm.value.roleAuth = defaultRoleInfoForm.roleAuth;
        flag = false;
        (<any>window).$message.warning("角色权限名不能为空");
    }
    if (flag) {
        await saveOrUpdateRole({
            isEnable: roleInfoForm.value?.isEnable as boolean,
            roleAuth: roleInfoForm.value?.roleAuth as string,
            roleDesc: roleInfoForm.value?.roleDesc as string,
        }).then((resp) => {
            if (resp.status) {
                showSaveNewRoleModal.value = false
                roleData.value.length = 0
                createRoleData({current: 1})
                tipMessage.type = 'success'
                tipMessage.content = resp.message
            }
        }).catch(() => {
            tipMessage.type = 'error'
            tipMessage.content = '保存失败'
        })
    } else {
        tipMessage.type = 'warning'
        tipMessage.content = '保存失败'
    }
}

// 点击修改角色信息按钮处理
const showRoleInfoModalHandle = (role: RoleBackModel) => {
    roleInfoForm.value = {
        isEnable: role.isEnable,
        menuIdList: role.menuIdList,
        resourceIdList: role.resourceIdList,
        roleAuth: role.roleAuth,
        roleDesc: role.roleDesc,
        roleId: role.id
    }
    defaultRoleInfoForm.roleDesc = role.roleDesc
    defaultRoleInfoForm.roleAuth = role.roleAuth
    defaultRoleInfoForm.isEnable = role.isEnable
    showRoleInfoModal.value = true
}

// 打开新增角色模态框
const showSaveNewRoleModalHandle = () => {
    roleInfoForm.value = {
        roleId: '',
        roleDesc: '',
        roleAuth: '',
        isEnable: false,
        resourceIdList: [],
        menuIdList: []
    }
    showSaveNewRoleModal.value = true
}

// 判断表单是否有改变
const checkRoleInfoFormStatusComputed = computed(() => {
    let desc = roleInfoForm.value?.roleDesc === defaultRoleInfoForm.roleDesc;
    let auth = roleInfoForm.value?.roleAuth === defaultRoleInfoForm.roleAuth;
    let enable = roleInfoForm.value?.isEnable === defaultRoleInfoForm.isEnable;
    return desc && auth && enable
})

// 删除角色处理
const deleteRoleHandle = (role: RoleBackModel) => {
    (<any>window).$dialog.warning({
        title: '警告',
        content: `你确定删除'${role.roleDesc}'吗？`,
        positiveText: '确定',
        negativeText: '算了',
        onPositiveClick: async () => {
            let tipMessage = (window as any).$message.create("保存中...", {
                type: 'loading'
            });
            const form = new FormData()
            form.append('roleId', role.id)
            await deleteRole(form).then((resp: ResultObject<null>) => {
                if (resp.status) {
                    roleData.value.length = 0
                    createRoleData({current: 1})
                    tipMessage.type = 'success'
                    tipMessage.content = resp.message
                }
            }).catch(() => {
                tipMessage.type = 'error'
                tipMessage.content = '删除失败'
            })
        },
    })
}

export {
    roleData,
    defaultTabValue,
    createRoleData,
    roleResourceData,
    createRoleResourceData,
    roleMenuData,
    createRoleMenuData,
    changeTabHandle,
    updateResourceCheckedKeys,
    updateMenuCheckedKeys,
    updateResourceHandle,
    updateMenuHandle,
    showRoleInfoModal,
    showRoleInfoModalHandle,
    updateRoleInfoHandle,
    roleInfoForm,
    checkRoleInfoFormStatusComputed,
    showSaveNewRoleModal,
    showSaveNewRoleModalHandle,
    saveNewRoleHandle,
    deleteRoleHandle
}