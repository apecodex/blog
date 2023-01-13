import type {DataTableColumns} from 'naive-ui'
import {h, nextTick, reactive, ref} from 'vue'
import {NTag, NButton, NImage, NText, NSpace, NA, NSpin} from 'naive-ui'
import {avatarFallbackSrc} from "~/components/common/constant";
import {Mail, TencentQq, CheckOne, Forbid, CloseOne, Local, Unlock, Lock} from '@icon-park/vue-next'
import {listUserBack, updateUserDisabledStatus} from "~/api/requests/UserAuth";
import {getRoleOptions} from '~/api/requests/Role'
import {StatusCode} from "~/api/enum/statusCode";
import {generateMap} from '~/utils/Msp'
import {isMobile} from '~/utils/utils'
import {updateUserRole} from "~/api/requests/UserInfo";
import {sendNotice} from "~/api/requests/Notice";

// 加载
const loading = ref(false)
// 显示地图
const showMapModal = ref(false)
// 显示修改角色
const showRoleOptionModal = ref(false);
// 显示发送通知
const showSendNoticeModal = ref(false);
// 用户角色选项
const roleOptions: { userId: string, roles: Array<{ id: string, roleDesc: string, checked: boolean }> } = reactive({
    userId: "",
    roles: []
})
// 加载角色
const roleOptionLoading = ref(false)
// 后台用户数据
const usersData: UsersBackModel[] = reactive([])
// 搜索数据
const searchData: ConditionParams = reactive({})
// 发送通知表单
const sendNoticeForm = reactive({
    content: "",
    title: "",
    url: "",
    userId: ""
})
// 分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 个用户`
    }
})

// 搜索用户
const searchUserHandle = async () => {
    const reEmail = /^[\w\-\.]+@[a-z0-9]+(\-[a-z0-9]+)?(\.[a-z0-9]+(\-[a-z0-9]+)?)*\.[a-z]{2,4}$/i
    const data: ConditionParams = {}
    if (reEmail.test(searchData.keywords as string)) {
        data.email = searchData.keywords
    } else {
        data.keywords = searchData.keywords
    }
    await createUsersData(data)
}

const createColumns = ({userData}: { userData: (row: UsersBackModel) => void }): DataTableColumns<UsersBackModel> => {
    return [
        {
            title: '用户昵称',
            key: 'nickname',
            width: 150,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {type: 'info'}, {
                    default: () => row.nickname
                })
            }
        },
        {
            title: 'UID',
            key: 'uid',
            width: 100,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {type: 'warning'}, {
                    default: () => row.uid
                })
            }
        },
        {
            title: '邮箱',
            key: 'email',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return row.email ? h(NText, {type: 'success'}, {
                    default: () => row.email
                }): h(NTag, {bordered: false, type: 'error'}, {
                    default: () => "未绑定"
                })
            }
        },
        {
            title: '头像',
            key: 'avatar',
            width: 70,
            align: 'center',
            render(row) {
                return h(NImage, {
                    width: 30,
                    src: row.avatar,
                    fallbackSrc: avatarFallbackSrc,
                    showToolbarTooltip: true,
                    objectFit: "contain",
                    style: {height: '30px', borderRadius: '4px'},
                    lazy: true,
                    intersectionObserverOptions: {root: '#userList-scroll-container'}
                }, {
                    placeholder: () => h(NSpin, {size: 24})
                })
            }
        },
        {
            title: '简介',
            key: 'intro',
            width: 150,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => (row.intro as string).length == 0 ? "该用户啥也没写~" : row.intro
                })
            }
        },
        {
            title: '个人网站',
            key: 'webSite',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {type: 'success', italic: true}, {
                    default: () => h(NA, {href: row.webSite, target: '_blank'}, {
                        default: () => row.webSite
                    })
                })
            }
        },
        {
            title: '登录方式',
            key: 'loginType',
            width: 100,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const textInfo = {text: "", type: 'info', icon: null}
                switch (row.loginType) {
                    case 0:
                        textInfo.text = "邮箱"
                        textInfo.type = "info"
                        textInfo.icon = Mail as any
                        break;
                    case 1:
                        textInfo.text = "QQ"
                        textInfo.type = "success"
                        textInfo.icon = TencentQq as any
                        break;

                }
                return h(NTag, {type: textInfo.type as any, size: 'small'}, {
                    default: () => textInfo.text,
                    icon: () => h(textInfo.icon as any)
                })
            }
        },
        {
            title: '角色',
            key: 'roleList',
            width: 120,
            align: 'center',
            render(row) {
                return h(NSpace, {justify: 'center'}, {
                    default: () =>
                        row.roleList.map((role) => {
                            const roleType = role.roleDesc === "管理员" ? "error" : role.roleDesc === "用户" ? "info" : "warning";
                            return h(NTag, {type: roleType, bordered: false, size: 'small'}, {
                                default: () => role.roleDesc
                            })
                        })
                })
            }
        },
        {
            title: 'IP',
            key: 'ipAddress',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.ipAddress
                })
            }
        },
        {
            title: '来源',
            key: 'ipSource',
            width: 180,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.ipSource
                })
            }
        },
        {
            title: '最后一次登录位置',
            key: 'rectangle',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                if (row.rectangle === null || row.rectangle.split(",")[0] === "null") {
                    return;
                }
                return h(NButton, {
                    text: true, type: "info", onClick: () => {
                        showMapModal.value = true
                        nextTick(() => {
                            generateMap("LastLoginMap", row.rectangle?.split(",")[0], row.rectangle?.split(",")[1])
                        })
                    }
                }, {
                    default: () => "查看位置",
                    icon: () => h(Local)
                })
            }
        },
        {
            title: 'QQ绑定',
            key: 'bindQQ',
            width: 90,
            align: 'center',
            render(row) {
                return h(NTag, {type: row.bindQQ ? "success" : "error", size: 'small'}, {
                    default: () => row.bindQQ ? "已绑定" : "未绑定",
                    icon: () => row.bindQQ ? h(Lock) : h(Unlock)
                })
            }
        },
        {
            title: '邮件提醒',
            key: 'isEmailNotice',
            width: 90,
            align: 'center',
            render(row) {
                return h(NTag, {type: row.isEmailNotice ? "success" : "error", size: 'small'}, {
                    default: () => row.isEmailNotice ? "开启" : "关闭",
                    icon: () => row.isEmailNotice ? h(CheckOne) : h(CloseOne)
                })
            }
        },
        {
            title: '状态',
            key: 'enable',
            width: 90,
            align: 'center',
            render(row) {
                return h(NTag, {type: row.enable ? "success" : "error", size: 'small'}, {
                    default: () => row.enable ? "正常" : "禁用",
                    icon: () => row.enable ? h(CheckOne) : h(Forbid)
                })
            }
        },
        {
            title: '创建时间',
            key: 'createTime',
            width: 170,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.createTime
                })
            }
        },
        {
            title: '最后登录时间',
            key: 'lastLoginTime',
            width: 170,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.lastLoginTime
                })
            }
        },
        {
            title: '操作',
            key: 'options',
            width: 200,
            align: 'center',
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(NSpace, {justify: 'center'}, {
                    default: () => [
                        h(
                            NButton,
                            {
                                size: 'small', tertiary: true, type: row.enable ? "warning" : "success",
                                onClick: async () => {
                                    let tipMessage = (window as any).$message.create("正在修改...", {
                                        type: 'loading'
                                    });
                                    const form = new FormData()
                                    form.append("id", row.userAuthId)
                                    form.append("enable", !row.enable as any)
                                    await updateUserDisabledStatus(form).then((resp: ResultObject<null>) => {
                                        if (resp.status) {
                                            row.enable = !row.enable;
                                            tipMessage.type = 'success'
                                            tipMessage.content = resp.message
                                        }
                                        if (resp.code == StatusCode.FAIL) {
                                            tipMessage.type = 'warning'
                                            tipMessage.content = resp.message
                                        }
                                    }).catch(() => {
                                        tipMessage.type = 'error'
                                        tipMessage.content = '修改失败'
                                    })
                                }
                            },
                            {default: () => row.enable ? "禁用" : "启用"}
                        ),
                        h(
                            NButton,
                            {
                                size: 'small', tertiary: true, type: 'tertiary',
                                onClick: () => {
                                    showSendNoticeModal.value = true;
                                    sendNoticeForm.userId = row.userInfoId;
                                }
                            },
                            {
                                default: () => "通知"
                            }
                        ),
                        h(
                            NButton,
                            {
                                size: 'small', tertiary: true, type: 'info',
                                onClick: async () => {
                                    showRoleOptionModal.value = true
                                    roleOptionLoading.value = true
                                    roleOptions.roles.length = 0
                                    roleOptions.userId = row.userAuthId
                                    await getRoleOptions().then((resp: ResultObject<Array<roleOptionModel>>) => {
                                        if (resp.status) {
                                            resp.data.map((role: roleOptionModel) => {
                                                const hasRole = row.roleList.filter((r) => r.id === role.id)
                                                roleOptions.roles.push({
                                                    id: role.id,
                                                    roleDesc: role.roleDesc,
                                                    checked: hasRole.length !== 0,
                                                })
                                            })
                                        }
                                        if (resp.code === StatusCode.FAIL) {
                                            (window as any).$message.warning(resp.message);
                                        }
                                    }).catch(() => {
                                        (window as any).$message.error("角色列表获取失败");
                                    }).finally(() => {
                                        roleOptionLoading.value = false
                                    })
                                }
                            },
                            {default: () => '修改角色'}
                        )
                    ]
                })
            }
        }
    ]
}

const createUsersData = async (condition: ConditionParams) => {
    usersData.length = 0
    loading.value = true
    await listUserBack(condition).then((resp: PageResult<Array<UsersBackModel>>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status) {
            resp.data.recordList.map((user: UsersBackModel) => {
                usersData.push({
                    userAuthId: user.userAuthId,
                    userInfoId: user.userInfoId,
                    uid: user.uid,
                    nickname: user.nickname,
                    email: user.email,
                    avatar: user.avatar,
                    intro: user.intro,
                    webSite: user.webSite,
                    bindQQ: user.bindQQ,
                    isEmailNotice: user.isEmailNotice,
                    createTime: user.createTime,
                    enable: user.enable,
                    loginType: user.loginType,
                    roleList: user.roleList,
                    ipAddress: user.ipAddress,
                    ipSource: user.ipSource,
                    rectangle: user.rectangle,
                    lastLoginTime: user.lastLoginTime
                })
            })
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch(() => {
        (window as any).$message.error("获取用户列表失败，请重试");
    }).finally(() => {
        loading.value = false
    })
}

const columns = createColumns({
    userData(row: UsersBackModel) {
    }
})

// 修改用户角色
const saveUserRoles = async () => {
    const checkedRole = roleOptions.roles.filter((role) => role.checked).map((roleId) => roleId.id)
    if (checkedRole.length === 0) {
        (window as any).$message.warning("至少有一个角色！");
        return;
    }
    let tipMessage = (window as any).$message.create("正在修改...", {
        type: 'loading'
    });
    await updateUserRole({roleList: checkedRole, userId: roleOptions.userId}).then((resp: ResultObject<null>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status) {
            tipMessage.type = 'success'
            tipMessage.content = resp.message
            createUsersData({current: pagination.page});
        }
        if (resp.code == StatusCode.FAIL) {
            tipMessage.type = 'warning'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '修改失败'
    }).finally(() => {
        showRoleOptionModal.value = false
    })
}

// 分页
const handlePageChange = async (currentPage: number) => {
    await createUsersData({current: currentPage});
}

// 发送通知
const sendNoticeHandle = async () => {
    let tipMessage = (window as any).$message.create("正在修改...", {
        type: 'loading'
    });
    if (sendNoticeForm.title.length === 0) {
        tipMessage.type = "warning";
        tipMessage.content = "通知标题不能为空"
        return;
    }
    if (sendNoticeForm.content.length === 0) {
        tipMessage.type = "warning";
        tipMessage.content = "通知内容不能为空"
        return;
    }
    const form = new FormData();
    form.append("userId", sendNoticeForm.userId);
    form.append("title", sendNoticeForm.title);
    form.append("content", sendNoticeForm.content);
    await sendNotice(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            tipMessage.type = "success";
            tipMessage.content = resp.message;
        } else {
            if (resp.code === StatusCode.FAIL) {
                tipMessage.type = "warning";
                tipMessage.content = resp.message;
            }
            sendNoticeForm.title = ""
            sendNoticeForm.content = ""
        }
    }).catch(() => {
        tipMessage.type = "warning";
        tipMessage.content = "发送通知失败，请重试"
    }).finally(() => {
        showSendNoticeModal.value = false;
    })
}

export {
    columns,
    usersData,
    searchData,
    searchUserHandle,
    createUsersData,
    pagination,
    loading,
    handlePageChange,
    showMapModal,
    showRoleOptionModal,
    showSendNoticeModal,
    roleOptions,
    roleOptionLoading,
    saveUserRoles,
    sendNoticeForm,
    sendNoticeHandle
}