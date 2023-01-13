import type {Ref} from 'vue'
import {h, ref, reactive} from 'vue'
import type {DataTableColumns, DataTableRowKey} from 'naive-ui'
import {NText, NTag, NAvatar, NButton, NPopover, NScrollbar} from 'naive-ui'
import {listOperationBack, deleteOperation} from '~/api/requests/Logs'
import {isMobile} from "~/utils/utils";
import {avatarFallbackSrc} from "~/components/common/constant";

// 加载
const loading = ref(false)
const operationData: Ref<Array<OperationLogBack>> = ref<Array<OperationLogBack>>([])
const checkedRowKeys = ref<DataTableRowKey[]>([])

// 操作日志分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 条`
    }
})

const createColumns = ({operation}: { operation: (row: OperationLogBack) => void }): DataTableColumns<OperationLogBack> => {
    return [
        {
            type: 'selection',
            width: 40,
            align: 'center',
            fixed: (isMobile() ? false : 'left') as any,
        },
        {
            title: '操作模块',
            key: 'optModule',
            width: 120,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NText, {strong: true, depth: 1}, {
                    default: () => row.optModule
                })
            }
        },
        {
            title: '操作URL',
            key: 'optUrl',
            width: 140,
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NText, {type: 'success', depth: 3, italic: true}, {
                    default: () => row.optUrl
                })
            }
        },
        {
            title: '操作类名',
            width: 100,
            key: 'optMethod',
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NText, {italic: true, depth: 2}, {
                    default: () => row.optMethod
                })
            }
        },
        {
            title: '操作说明',
            key: 'optDesc',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NText, {type: 'warning', depth: 3}, {
                    default: () => row.optDesc
                })
            }
        },
        {
            title: '请求参数',
            type: 'expand',
            width: 90,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            renderExpand: (row) => {
                return h(NScrollbar, {style: {maxHeight: '100px'}}, {
                    default: () => h(NText, {depth: 3, italic: true}, {
                        default: () => row.requestParam
                    })
                })
            }
        },
        {
            title: '响应结果',
            key: 'responseData',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: false
            },
            render(row) {
                return h(NPopover, {
                    scrollable: true,
                    placement: "top-start",
                    style: {maxWidth: '240px', maxHeight: '140px'}
                }, {
                    trigger: () => row.responseData,
                    default: () => {
                        const d = JSON.parse(row.responseData) as ResultObject<any>
                        return `
                        code: ${d.code}
                        status: ${d.status}
                        message: ${d.message}
                        ${d.data !== undefined ? 'data: ' + d.data : ''}
                        `
                    }
                })
            }
        },
        {
            title: '操作类型',
            key: 'optType',
            width: 110,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                let color: { color?: string, borderColor?: string, textColor?: string } = {}
                switch (row.optType) {
                    case '删除':
                        color.textColor = '#EA2027'
                        color.borderColor = '#EA2027'
                        break
                    case '新增':
                        color.textColor = '#009432'
                        color.borderColor = '#009432'
                        break
                    case '修改':
                        color.textColor = '#EE5A24'
                        color.borderColor = '#EE5A24'
                        break
                    case '新增或修改':
                        color.textColor = '#F79F1F'
                        color.borderColor = '#F79F1F'
                        break
                    case '评论':
                        color.textColor = '#FFC312'
                        color.borderColor = '#FFC312'
                        break
                }
                return h(NTag, {color, size: 'small'}, {
                    default: () => row.optType
                })
            }
        },
        {
            title: '请求方法',
            key: 'requestMethod',
            width: 100,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                let color: { color?: string, borderColor?: string, textColor?: string } = {}
                switch (row.requestMethod) {
                    case 'POST':
                        color.textColor = '#009432'
                        color.borderColor = '#009432'
                        break
                    case 'GET':
                        color.textColor = '#12CBC4'
                        color.borderColor = '#12CBC4'
                        break
                    case 'PUT':
                        color.textColor = '#EE5A24'
                        color.borderColor = '#EE5A24'
                        break
                    case 'DELETE':
                        color.textColor = '#EA2027'
                        color.borderColor = '#EA2027'
                        break
                }
                return h(NTag, {color, size: 'small'}, {
                    default: () => row.requestMethod
                })
            }
        },
        {
            title: '操作用户',
            key: 'user',
            width: 90,
            align: 'center',
            render(row) {
                return h(NPopover, {trigger: 'hover', style: {padding: '5px'}}, {
                    trigger: () => h(NAvatar, {size: 'small', src: row.user.avatar, fallbackSrc: avatarFallbackSrc}),
                    default: () => row.user.nickname
                })
            }
        },
        {
            title: 'IP',
            key: 'idAddress',
            width: 140,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.idAddress
                })
            }
        },
        {
            title: '来源',
            key: 'ipSource',
            width: 170,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.ipSource
                })
            }
        },
        {
            title: '创建时间',
            key: 'createTime',
            width: 180,
            align: 'center',
            ellipsis: {
                tooltip: true
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
            width: 80,
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
                                    await deleteOperation({
                                        idList: new Array<string>(row.id),
                                        isDelete: true
                                    }).then((resp: ResultObject<null>) => {
                                        if (resp.status) {
                                            createOperationData({current: pagination.page})
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

const createOperationData = async (condition: ConditionParams) => {
    loading.value = true
    operationData.value.length = 0
    await listOperationBack(condition).then((resp: PageResult<Array<OperationLogBack>>) => {
        if (resp.status && resp.data.count) {
            operationData.value = Array.from(resp.data.recordList)
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch(() => {
        (<any>window).$message.error("获取操作日志失败，请重试");
    }).finally(() => {
        loading.value = false
    })
}

const columns = createColumns({
    operation(row: OperationLogBack) {
    }
})

// 分页处理
const handlePageChange = async (currentPage: number) => {
    await createOperationData({current: currentPage});
}

// 勾选
const handleCheck = (rowKeys: DataTableRowKey[]) => {
    checkedRowKeys.value = rowKeys
}

// 批量删除
const deleteCheckedOperationLog = () => {
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
                await deleteOperation({
                    idList: Array.from(checkedRowKeys.value) as Array<string>,
                    isDelete: true
                }).then((resp: ResultObject<null>) => {
                    if (resp.status) {
                        checkedRowKeys.value.length = 0 as any
                        tipMessage.type = 'success'
                        tipMessage.content = resp.message
                        createOperationData({current: pagination.page})
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
    operationData,
    columns,
    createOperationData,
    pagination,
    loading,
    handlePageChange,
    handleCheck,
    checkedRowKeys,
    deleteCheckedOperationLog
}