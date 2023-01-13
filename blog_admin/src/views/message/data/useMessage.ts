import type {DataTableColumns} from 'naive-ui'
import type {Ref} from 'vue'
import {h, reactive, ref} from 'vue'
import {
    DataTableRowKey,
    NAvatar,
    NButton,
    NPopconfirm,
    NPopover,
    NSpace,
    NTag,
    NText,
    SelectOption
} from 'naive-ui'
import {avatarFallbackSrc} from "~/components/common/constant";
import {CheckOne, CloseOne} from "@icon-park/vue-next";
import {listMessageBack, updateMessageReview, deleteMessage} from "~/api/requests/Message";
import {StatusCode} from "~/api/enum/statusCode";
import {isMobile} from "~/utils/utils";

const searchData: ConditionParams = reactive({
    type: null,
    isReview: null,
    current: 1,
})
const messageTheme = {
    default: '#1e90ff',
    orange: '#ffa502',
    FlamingoPink: '#f78fb3',
    tomato: '#ff6348',
    watermelon: '#ff4757',
    PrestigeBlue: '#2f3542',
    UfoGreen: '#2ed573',
    BrightGreek: '#3742fa',
    wisteria: '#8e44ad'
} as any
// 加载
const loading = ref(false)
// 已选中的行
const checkedRowKeys = ref<DataTableRowKey[]>([])
const messageData: Ref<Array<MessageBackModel>> = ref<Array<MessageBackModel>>([])
const deleteId = ref("")
// 留言分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 条`
    }
})

// 搜索评论处理
const searchMessageHandle = async (current?: number) => {
    const data: ConditionParams = {}
    if (searchData.type !== null) {
        data.type = searchData.type
    }
    if (searchData.isReview !== null) {
        data.isReview = searchData.isReview
    }
    if (current) {
        data.current = current
    }
    await createMessageData(data as ConditionParams)
}

// 留言用户搜索处理方法
const typeHandle = async (value: number, option: SelectOption) => {
    searchData.type = value
    await searchMessageHandle()
}
// 留言审核搜索处理方法
const reviewHandle = async (value: number, option: SelectOption) => {
    searchData.isReview = value
    await searchMessageHandle()
}
// 单个审核
const updateMessageReviewHandle = async (data: ReviewParams) => {
    return await updateMessageReview(data);
}

// 选中
const handleCheck = (rowKeys: DataTableRowKey[]) => {
    checkedRowKeys.value = rowKeys
}

// 修改选择的审核（批量审核）
const updateCheckedReview = (isReview: boolean) => {
    if (checkedRowKeys.value.length !== 0) {
        let tipMessage = (window as any).$message.create("正在修改...", {
            type: 'loading'
        });
        updateMessageReviewHandle({idList: [...checkedRowKeys.value] as Array<string>, isReview}).then(async (resp) => {
            if (resp.status) {
                checkedRowKeys.value.length = 0 as any
                tipMessage.type = 'success'
                tipMessage.content = resp.message
                await searchMessageHandle(pagination.page)
            }
        }).catch(() => {
            tipMessage.type = 'error'
            tipMessage.content = '修改失败'
        })
    }
}

// 通过id删除留言
const deleteMessageByIds = async (ids: Array<string>) => {
    let tipMessage = (window as any).$message.create("正在删除...", {
        type: 'loading'
    });
    await deleteMessage(ids).then((resp: ResultObject<null>) => {
        if (resp.status) {
            checkedRowKeys.value.length = 0 as any
            createMessageData({current: pagination.page});
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        {
            tipMessage.type = 'error'
            tipMessage.content = '删除失败'
        }
    })
}
// 删除单个
const deleteMessageHandle = async () => {
    await deleteMessageByIds(new Array<string>(deleteId.value));
}
// 批量删除
const deleteChecked = async () => {
    await deleteMessageByIds([...checkedRowKeys.value] as Array<string>);
}

const createColumns = ({message}: { message: (row: MessageBackModel) => void }): DataTableColumns<MessageBackModel> => {
    return [
        {
            type: 'selection',
            width: 8,
            align: 'center',
            fixed: (isMobile() ? false : 'left') as any,
        },
        {
            title: '昵称',
            key: 'nickname',
            width: 30,
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
            title: '用户',
            key: 'user',
            width: 20,
            align: 'center',
            render(row) {
                return row.user ? h(NPopover, {
                    trigger: 'hover',
                    style: {padding: '5px'}
                }, {
                    trigger: () => h(NAvatar, {
                        size: 'small',
                        color: "transparent",
                        src: (row.user as SimpleUserInfoModel).avatar,
                        fallbackSrc: avatarFallbackSrc
                    }),
                    default: () => h(NSpace, {vertical: true, style: {fontSize: '12px'}}, {
                        default: () => [
                            'UID: ' + (row.user as SimpleUserInfoModel).uid,
                            '昵称: ' + (row.user as SimpleUserInfoModel).nickname
                        ]
                    })
                }) : null
            }
        },
        {
            title: '留言内容',
            key: 'content',
            width: 60,
            align: 'center',
            ellipsis: {
                tooltip: false
            },
            render(row) {
                return h(NPopover, {
                    scrollable: true,
                    placement: "top-start",
                    style: {maxWidth: '240px', maxHeight: '140px', wordBreak: 'break-all'}
                }, {
                    trigger: () => h(NText, {depth: 3}, {
                        default: () => row.content
                    }),
                    default: () => row.content
                })
            }
        },
        {
            title: '主题',
            key: 'theme',
            width: 20,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NPopover, {trigger: 'hover'}, {
                    trigger: () => h(NButton, {color: messageTheme[row.theme], style: {width: '50px', height: '15px'}}),
                    default: () => row.theme
                })
            }
        },
        {
            title: 'IP',
            key: 'ipAddress',
            width: 40,
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
            width: 50,
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
            title: '留言时间',
            key: 'createTime',
            width: 40,
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
            title: '审核',
            key: 'isReview',
            width: 25,
            align: 'center',
            render(row) {
                return h(NTag, {type: row.isReview ? 'warning' : 'success'}, {
                    default: () => row.isReview ? "未审核" : "已审核",
                    icon: () => row.isReview ? h(CloseOne) : h(CheckOne)
                })
            }
        },
        {
            title: '操作',
            key: 'options',
            width: 30,
            align: 'center',
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(NSpace, {justify: 'center'}, {
                    default: () => [
                        !row.isReview ? h(NButton, {
                            size: 'small', tertiary: true, type: 'warning', onClick: () => {
                                let tipMessage = (window as any).$message.create("保存中...", {
                                    type: 'loading'
                                });
                                updateMessageReviewHandle({
                                    idList: new Array<string>(row.id),
                                    isReview: true
                                }).then((resp: ResultObject<null>) => {
                                    if (resp.status) {
                                        checkedRowKeys.value.length = 0 as any
                                        row.isReview = true as any
                                        tipMessage.type = 'success'
                                        tipMessage.content = resp.message
                                    }
                                }).catch(() => {
                                    tipMessage.type = 'error'
                                    tipMessage.content = '修改失败'
                                })
                            }
                        }, {
                            default: () => "待审"
                        }) : h(NButton, {
                            size: 'small', tertiary: true, type: 'info', onClick: () => {
                                let tipMessage = (window as any).$message.create("保存中...", {
                                    type: 'loading'
                                });
                                updateMessageReviewHandle({
                                    idList: new Array<string>(row.id),
                                    isReview: false
                                }).then((resp: ResultObject<null>) => {
                                    if (resp.status) {
                                        checkedRowKeys.value.length = 0 as any
                                        row.isReview = false as any
                                        tipMessage.type = 'success'
                                        tipMessage.content = resp.message
                                    }
                                }).catch(() => {
                                    tipMessage.type = 'error'
                                    tipMessage.content = '修改失败'
                                })
                            }
                        }, {
                            default: () => "审核"
                        }),
                        h(NPopconfirm, {onPositiveClick: deleteMessageHandle}, {
                            trigger: () => h(NButton, {
                                size: 'small', tertiary: true, type: 'error', onClick() {
                                    deleteId.value = row.id
                                }
                            }, {default: () => "删除"}),
                            default: () => "确定删除吗？"
                        })
                    ]
                })
            }
        }
    ]
}

const createMessageData = async (condition: ConditionParams) => {
    loading.value = true
    messageData.value.length = 0
    await listMessageBack(condition).then((resp: PageResult<Array<MessageBackModel>>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status) {
            messageData.value = Array.from(resp.data.recordList)
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch(() => {
        (window as any).$message.error("获取留言列表失败，请重试！");
    }).finally(() => {
        loading.value = false
    })
}

const columns = createColumns({
    message(row: MessageBackModel) {
    }
})

// 分页
const handlePageChange = async (currentPage: number) => {
    await createMessageData({current: currentPage});
}
export {
    searchData,
    typeHandle,
    reviewHandle,
    columns,
    messageData,
    createMessageData,
    handleCheck,
    updateCheckedReview,
    deleteChecked,
    checkedRowKeys,
    pagination,
    handlePageChange,
    loading,
}