import type {Ref} from 'vue'
import type {DataTableColumns, DataTableRowKey} from 'naive-ui'
import {h, reactive, ref} from 'vue'
import {NAvatar, NButton, NPopover, NTag, NSpace, SelectOption, NPopconfirm, NText, NInput} from 'naive-ui'
import {FriendsCircle, Word, CheckOne, CloseOne} from '@icon-park/vue-next'
import {avatarFallbackSrc} from '~/components/common/constant'
import {listCommentBack, updateCommentReview, deleteComment} from '~/api/requests/Comment'
import {StatusCode} from "~/api/enum/statusCode";
import {isMobile, parseComment} from "~/utils/utils";

const searchData: ConditionParams = reactive({
    keywords: "",
    type: null,
    isReview: null,
    current: 1,
})

// 加载
const loading = ref(false)
// 已选中的行
const checkedRowKeys = ref<DataTableRowKey[]>([])
// 评论数据
const commentData: Ref<Array<CommentBackModel>> = ref<Array<CommentBackModel>>([])
// 当前选中的id
const deleteId = ref("")

// 评论分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 条`
    }
})

// 搜索评论处理
const searchCommentHandle = async (current?: number) => {
    const data: ConditionParams = {}
    if (searchData.keywords?.length !== 0) {
        data.keywords = searchData.keywords
    }
    if (searchData.type !== null) {
        data.type = searchData.type
    }
    if (searchData.isReview !== null) {
        data.isReview = searchData.isReview
    }
    if (current) {
        data.current = current
    }
    await createCommentData(data as ConditionParams)
}

// 评论类型搜索处理方法
const typeHandle = async (value: number, option: SelectOption) => {
    searchData.type = value
    await searchCommentHandle()
}
// 评论审核搜索处理方法
const reviewHandle = async (value: number, option: SelectOption) => {
    searchData.isReview = value
    await searchCommentHandle()
}

// 选中
const handleCheck = (rowKeys: DataTableRowKey[]) => {
    checkedRowKeys.value = rowKeys
}

// 修改评论审核
const updateCommentReviewHandle = async (data: { idList: Array<string>, isReview: boolean }): Promise<ResultObject<null>> => {
    return await updateCommentReview(data);
}
// 修改选择的审核
const updateCheckedReview = (isReview: boolean) => {
    if (checkedRowKeys.value.length !== 0) {
        let tipMessage = (window as any).$message.create("正在修改...", {
            type: 'loading'
        });
        updateCommentReviewHandle({idList: [...checkedRowKeys.value] as Array<string>, isReview}).then(async (resp) => {
            if (resp.status) {
                checkedRowKeys.value.length = 0 as any
                tipMessage.type = 'success'
                tipMessage.content = resp.message
                await searchCommentHandle(pagination.page)
            }
        }).catch(() => {
            tipMessage.type = 'error'
            tipMessage.content = '修改失败'
        })
    }
}

// 删除评论
const deleteCommentHandle = async () => {
    let tipMessage = (window as any).$message.create("正在删除...", {
        type: 'loading'
    });
    const form = new FormData()
    form.append('commentId', deleteId.value)
    await deleteComment(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            createCommentData({current: pagination.page});
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch((error) => {
        tipMessage.type = 'error'
        tipMessage.content = '删除失败'
    })
}

const createColumns = ({comment}: { comment: (row: CommentBackModel) => void }): DataTableColumns<CommentBackModel> => {
    return [
        {
            type: 'selection',
            width: 15,
            align: 'center',
        },
        {
            title: '评论用户',
            key: 'user',
            width: 40,
            align: 'center',
            render(row) {
                return h(NPopover, {trigger: 'hover', style: {padding: '5px'}}, {
                    trigger: () => h(NAvatar, {size: 'small', src: row.user.avatar, fallbackSrc: avatarFallbackSrc}),
                    default: () => h(NSpace, {vertical: true, style: {fontSize: '12px'}}, {
                        default: () => [
                            'uid: ' + row.user.uid,
                            '昵称: ' + row.user.nickname
                        ]
                    })
                })
            }
        },
        {
            title: '回复用户',
            key: 'replyUser',
            width: 40,
            align: 'center',
            render(row) {
                return row.replyUser ? h(NPopover, {
                    trigger: 'hover',
                    placement: "bottom",
                    style: {padding: '5px'}
                }, {
                    trigger: () => h(NAvatar, {
                        size: 'small',
                        color: "transparent",
                        src: (row.replyUser as SimpleUserInfoModel).avatar,
                        fallbackSrc: avatarFallbackSrc
                    }),
                    default: () => h(NSpace, {vertical: true, style: {fontSize: '12px'}}, {
                        default: () => [
                            'uid: ' + (row.replyUser as SimpleUserInfoModel).uid,
                            '昵称: ' + (row.replyUser as SimpleUserInfoModel).nickname
                        ]
                    })
                }) : null
            }
        },
        {
            title: '评论内容',
            type: 'expand',
            width: 40,
            align: 'center',
            ellipsis: {
                tooltip: false
            },
            renderExpand: (rowData: CommentBackModel) => {
                return h('p', {class: 'border-1px border-dashed p-5px rounded-6px whitespace-pre-line max-h-100px max-w-600px <md:(!max-w-full) overflow-y-scroll',innerHTML: parseComment(rowData.commentContent, '24', '24')})
            }
        },
        {
            title: '文章标题',
            key: 'articleTitle',
            width: 60,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return row.articleTitle ? h(NText, {depth: 2}, {
                    default: () => `《${row.articleTitle}》`
                }) : "";
            }
        },
        {
            title: '类型',
            key: 'type',
            width: 40,
            align: 'center',
            render(row) {
                const t = row.type === "说说";
                return h(NTag, {color: {borderColor: 'transparent', textColor: t ? "#82589F" : "#F97F51"}}, {
                    default: () => row.type,
                    icon: () => t ? h(FriendsCircle) : h(Word)
                })
            }
        },
        {
            title: '回复量',
            key: 'replyCount',
            width: 40,
            align: 'center',
            render(row) {
                return h(NText, {}, {
                    default: () => row.replyCount ? row.replyCount : 0
                })
            }
        },
        {
            title: '点赞量',
            key: 'likeCount',
            width: 40,
            align: 'center',
            render(row) {
                return h(NText, {}, {
                    default: () => row.likeCount ? row.likeCount : 0
                })
            }
        },
        {
            title: 'ip来源',
            key: 'ipSource',
            width: 100,
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
            title: '浏览器',
            key: 'browser',
            width: 60,
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
            width: 60,
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
            title: '评论时间',
            key: 'createTime',
            width: 80,
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
            width: 50,
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
            width: 70,
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
                                updateCommentReviewHandle({
                                    idList: new Array<string>(row.id),
                                    isReview: true
                                }).then((resp) => {
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
                                updateCommentReviewHandle({
                                    idList: new Array<string>(row.id),
                                    isReview: false
                                }).then((resp) => {
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
                        h(NPopconfirm, {onPositiveClick: deleteCommentHandle}, {
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

const createCommentData = async (condition: ConditionParams) => {
    loading.value = true
    commentData.value.length = 0
    await listCommentBack(condition).then((resp: PageResult<Array<CommentBackModel>>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status && resp.data.recordList !== null) {
            commentData.value = Array.from(resp.data.recordList)
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch((error) => {
        (window as any).$message.error("获取评论列表失败，请重试！");
    }).finally(() => {
        loading.value = false
    })
}

const columns = createColumns({
    comment(row: CommentBackModel) {
    }
})

const handlePageChange = async (currentPage: number) => {
    await createCommentData({current: currentPage});
}

export {
    searchData,
    searchCommentHandle,
    typeHandle,
    reviewHandle,
    columns,
    handleCheck,
    checkedRowKeys,
    pagination,
    handlePageChange,
    loading,
    createCommentData,
    commentData,
    updateCheckedReview,
}