import type {Ref} from 'vue'
import {h, ref, reactive, nextTick} from 'vue'
import {DataTableRowKey, NAvatar, NButton, NPopover, NTag, NText, NSpace} from 'naive-ui'
import type {DataTableColumns} from 'naive-ui'
import {listLoginLogBack, deleteLoginLog} from '~/api/requests/Logs'
import {isMobile} from "~/utils/utils";
import {avatarFallbackSrc} from "~/components/common/constant";
import {Local, Mail, TencentQq} from "@icon-park/vue-next";
import {generateMap} from "~/utils/Msp";

const loading = ref(false)
const showLoginRectangleMap = ref(false)
const checkedRowKeys = ref<DataTableRowKey[]>([])

// 登录日志分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 条`
    }
})

const loginLogData: Ref<Array<LoginLogBack>> = ref<Array<LoginLogBack>>([])

const createColumns = ({loginLog}: { loginLog: (row: LoginLogBack) => void }): DataTableColumns<LoginLogBack> => {
    return [
        {
            type: 'selection',
            width: 40,
            align: 'center',
            fixed: (isMobile() ? false : 'left') as any,
        },
        {
            title: '用户',
            key: 'user',
            width: 70,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NPopover, {trigger: 'hover', style: {padding: '5px'}}, {
                    trigger: () => h(NAvatar, {size: 'small', src: row.user.avatar, fallbackSrc: avatarFallbackSrc}),
                    default: () => h(NSpace, {vertical: true, style: {fontSize: '12px'}}, {
                        default: () => [
                            'UID: ' + row.user.uid,
                            '昵称: ' + row.user.nickname
                        ]
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
                tooltip: true
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
            title: '登录位置',
            key: 'rectangle',
            width: 130,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                if (row.rectangle === null || row.rectangle.split(",")[0] === "null") {
                    return;
                }
                return h(NButton, {
                    text: true, type: "info", onClick: async () => {
                        showLoginRectangleMap.value = true
                        await nextTick(() => {
                            generateMap("LoginRectangleMap", row.rectangle?.split(",")[0], row.rectangle?.split(",")[1])
                        })
                    }
                }, {
                    default: () => "查看位置",
                    icon: () => h(Local)
                })
            }
        },
        {
            title: '浏览器',
            key: 'browser',
            width: 130,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.browser
                })
            }
        },
        {
            title: '操作系统',
            key: 'os',
            width: 120,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.os
                })
            }
        },
        {
            title: '登录时间',
            key: 'createTime',
            width: 190,
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
            title: '操作',
            key: 'actions',
            width: 100,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(
                    NButton,
                    {
                        strong: true,
                        tertiary: true,
                        type: 'error',
                        size: 'small',
                        onClick: () => {
                            (<any>window).$dialog.warning({
                                title: '警告',
                                content: '你确定删除吗？',
                                positiveText: '确定',
                                negativeText: '算了',
                                onPositiveClick: async () => {
                                    let tipMessage = (window as any).$message.create("正在删除...", {
                                        type: 'loading'
                                    });
                                    await deleteLoginLog({
                                        idList: new Array<string>(row.id),
                                        isDelete: true
                                    }).then((resp: ResultObject<null>) => {
                                        if (resp.status) {
                                            createLoginLogData({current: pagination.page})
                                            tipMessage.type = 'success'
                                            tipMessage.content = resp.message
                                        }
                                    }).catch(() => {
                                        tipMessage.type = 'error'
                                        tipMessage.content = '删除失败'
                                    })
                                }
                            })
                        }
                    },
                    {default: () => '删除'}
                )
            }
        }
    ]
}

const createLoginLogData = async (condition: ConditionParams) => {
    loading.value = true
    loginLogData.value.length = 0
    await listLoginLogBack(condition).then((resp: PageResult<Array<LoginLogBack>>) => {
        if (resp.status && resp.data.count) {
            loginLogData.value = Array.from(resp.data.recordList)
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch(() => {
        (<any>window).$message.error("获取登录日志失败，请重试");
    }).finally(() => {
        loading.value = false
    })
}

const columns = createColumns({
    loginLog(row: LoginLogBack) {
    }
})

// 分页处理
const handlePageChange = async (currentPage: number) => {
    await createLoginLogData({current: currentPage});
}

// 勾选
const handleCheck = (rowKeys: DataTableRowKey[]) => {
    checkedRowKeys.value = rowKeys
}

// 批量删除
const deleteCheckedLoginLog = () => {
    if (checkedRowKeys.value.length !== 0) {
        (<any>window).$dialog.warning({
            title: '警告',
            content: `确定删除这${checkedRowKeys.value.length}条日志吗？`,
            positiveText: '确定',
            negativeText: '算了',
            onPositiveClick: async () => {
                let tipMessage = (window as any).$message.create("正在删除...", {
                    type: 'loading'
                });
                await deleteLoginLog({
                    idList: Array.from(checkedRowKeys.value) as Array<string>,
                    isDelete: true
                }).then((resp: ResultObject<null>) => {
                    if (resp.status) {
                        checkedRowKeys.value.length = 0 as any
                        tipMessage.type = 'success'
                        tipMessage.content = resp.message
                        createLoginLogData({current: pagination.page})
                    }
                }).catch(() => {
                    tipMessage.type = 'error'
                    tipMessage.content = '删除失败'
                })
            }
        })
    }
}

export {
    loginLogData,
    columns,
    showLoginRectangleMap,
    pagination,
    loading,
    checkedRowKeys,
    handlePageChange,
    createLoginLogData,
    handleCheck,
    deleteCheckedLoginLog
}