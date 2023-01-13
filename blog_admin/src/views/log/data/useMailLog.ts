import type {Ref} from 'vue'
import {h, ref, reactive} from 'vue'
import {DataTableRowKey, NButton, NTag, NText, SelectOption} from 'naive-ui'
import type {DataTableColumns} from 'naive-ui'
import {deleteMailLog, listMailLogBack} from '~/api/requests/Logs'
import {isMobile} from "~/utils/utils";

const loading = ref(false)
// 被勾选的数据
const checkedRowKeys = ref<DataTableRowKey[]>([])

// 邮件日志分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 条`
    }
})

const mailLogData: Ref<Array<MailLogBack>> = ref<Array<MailLogBack>>([])
// 搜索数据
const searchData: Ref<ConditionParams> = ref<ConditionParams>({
    type: null,
})

// 搜索处理
const searchMailLogHandle = async () => {
    const data: ConditionParams = {}
    if (searchData.value.type !== null) {
        data.type = searchData.value.type
    }
    data.current = 1
    await createMailLogData(data)
}

// 搜索邮件类型
const typeHandle = async (value: number, option: SelectOption) => {
    searchData.value.type = value
    await searchMailLogHandle()
}

const createColumns = ({mailLog}: { mailLog: (row: MailLogBack) => void }): DataTableColumns<MailLogBack> => {
    return [
        {
            type: 'selection',
            width: 40,
            align: 'center',
            fixed: (isMobile() ? false : 'left') as any,
        },
        {
            title: '邮箱',
            key: 'email',
            width: 150,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {type: 'warning', strong: true}, {
                    default: () => row.email
                })
            }
        },
        {
            title: '类型',
            key: 'type',
            width: 100,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                let color: { color?: string, borderColor?: string, textColor?: string } = {}
                switch (row.type) {
                    case '评论':
                        color.textColor = '#009432'
                        color.borderColor = '#009432'
                        break
                    case '留言':
                        color.textColor = '#12CBC4'
                        color.borderColor = '#12CBC4'
                        break
                    case '验证码':
                        color.textColor = '#EE5A24'
                        color.borderColor = '#EE5A24'
                        break
                    case '通知':
                        color.textColor = '#EA2027'
                        color.borderColor = '#EA2027'
                        break
                }
                return h(NTag, {color, size: 'small'}, {
                    default: () => row.type
                })
            }
        },
        {
            title: '投递状态',
            key: 'status',
            width: 100,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                let statusText = ''
                let color: { color?: string, borderColor?: string, textColor?: string } = {}
                switch (row.status) {
                    case 0:
                        color.textColor = '#12CBC4'
                        color.borderColor = '#12CBC4'
                        statusText = '投递中'
                        break
                    case 1:
                        color.textColor = '#009432'
                        color.borderColor = '#009432'
                        statusText = '投递成功'
                        break
                    case 2:
                        color.textColor = '#EA2027'
                        color.borderColor = '#EA2027'
                        statusText = '投递失败'
                        break
                }
                return h(NTag, {color, size: 'small'}, {
                    default: () => statusText
                })
            }
        },
        {
            title: '路由键',
            key: 'routeKey',
            width: 160,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.routeKey
                })
            }
        },
        {
            title: '交换机',
            key: 'exchange',
            width: 160,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.exchange
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
                return h(NText, {depth: 3}, {
                    default: () => row.createTime
                })
            }
        },
        {
            title: '操作',
            key: 'options',
            width: 90,
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
                                    await deleteMailLog({
                                        idList: new Array<string>(row.msgId),
                                        isDelete: true
                                    }).then((resp: ResultObject<null>) => {
                                        if (resp.status) {
                                            createMailLogData({current: pagination.page})
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

const createMailLogData = async (condition: ConditionParams) => {
    loading.value = true
    mailLogData.value.length = 0
    await listMailLogBack(condition).then((resp: PageResult<Array<MailLogBack>>) => {
        if (resp.status && resp.data.count) {
            mailLogData.value = Array.from(resp.data.recordList)
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch(() => {
        (<any>window).$message.error("获取邮件列表失败，请重试");
    }).finally(() => {
        loading.value = false
    })
}

const columns = createColumns({
    mailLog(row: MailLogBack) {
    }
})

// 分页
const handlePageChange = async (currentPage: number) => {
    await createMailLogData({current: currentPage})
}

// 勾选
const handleCheck = (rowKeys: DataTableRowKey[]) => {
    checkedRowKeys.value = rowKeys
}

// 批量删除
const deleteCheckedMailLog = () => {
    console.log(checkedRowKeys.value)
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
                await deleteMailLog({
                    idList: Array.from(checkedRowKeys.value) as Array<string>,
                    isDelete: true
                }).then((resp: ResultObject<null>) => {
                    if (resp.status) {
                        checkedRowKeys.value.length = 0 as any
                        tipMessage.type = 'success'
                        tipMessage.content = resp.message
                        createMailLogData({current: pagination.page})
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
    mailLogData,
    columns,
    createMailLogData,
    checkedRowKeys,
    pagination,
    loading,
    handlePageChange,
    handleCheck,
    deleteCheckedMailLog,
    searchData,
    typeHandle
}