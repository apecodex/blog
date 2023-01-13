import {h, reactive, ref} from 'vue'
import {NAvatar, NButton, NInput, NPopover, NSpace, NSwitch, NTag, NPopconfirm, SelectOption, NText} from 'naive-ui'
import type {DataTableColumns} from 'naive-ui'
import {avatarFallbackSrc} from "~/components/common/constant";
import {Picture} from "@icon-park/vue-next";
import {listTalkBack, deleteTalk, updateTalkTop} from "~/api/requests/Talk";
import {StatusCode} from "~/api/enum/statusCode";
import {router} from "~/router";
import {isMobile, parseComment} from "~/utils/utils";

type TalkBackType = {
    user: SimpleUserInfoModel,
    id: string,
    content: string,
    isTop: boolean,
    status: number,
    pictureVideos: Array<TalkPictureVideoModel>,
    likeCount: null | number,
    commentCount: null | number,
    createTime: string,
    updateTime: string,
    loading: boolean
}

// 加载
const loading = ref(false)
// 说说分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 条`
    }
})

// 文章搜索关键字
const searchData: ConditionParams = reactive({
    keywords: "",
    status: null,
    current: 1,
})

// 说说配图展示
const showTalkPictureVideoModal = ref(false)
// 当前行的说说配图列表
const talkPictureVideos = ref<Array<TalkPictureVideoModel>>([])
// 说说数据列表
const talkBackData: TalkBackType[] = reactive([])
// 待删除的说说的id
const deleteId = ref("")

// 搜索说说处理
const searchTalkHandle = async (current?: number) => {
    const data: ConditionParams = {}
    if (searchData.keywords?.length !== 0) {
        data.keywords = searchData.keywords
    }
    if (searchData.status !== null) {
        data.status = searchData.status
    }
    if (current) {
        data.current = current
    }
    await createTalkData(data as ConditionParams)
}

// 文章状态处理方法
const statusHandle = async (value: number, option: SelectOption) => {
    searchData.status = value
    await searchTalkHandle()
}

// 删除说说处理
const deleteTalkHandle = async () => {
    let tipMessage = (window as any).$message.create("正在删除...", {
        type: 'loading'
    });
    const form = new FormData()
    form.append("talkId", deleteId.value)
    await deleteTalk(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            createTalkData({current: pagination.page});
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '删除失败'
    })
}

const createColumns = ({talk}: { talk: (row: TalkBackType) => void }): DataTableColumns<TalkBackType> => {
    return [
        {
            title: '用户',
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
            title: '说说内容',
            type: 'expand',
            width: 60,
            align: 'center',
            ellipsis: {
                tooltip: false
            },
            renderExpand: (rowData: TalkBackType) => {
                return h('p', {class: 'border-1px border-dashed p-5px rounded-6px whitespace-pre-line max-h-100px max-w-600px <md:(!max-w-full) overflow-y-scroll',innerHTML: parseComment(rowData.content, '24', '24')})
            }
        },
        {
            title: '配图',
            key: 'pictureVideos',
            width: 80,
            align: 'center',
            render(row) {
                return row.pictureVideos.length !== 0 ? h(NButton, {
                    secondary: true, strong: true, type: 'tertiary', size: 'small', onClick: () => {
                        showTalkPictureVideoModal.value = true
                        talkPictureVideos.value = row.pictureVideos
                    },
                    style: {width: '100px'}
                }, {
                    icon: () => h(Picture),
                    default: () => "预览配图"
                }) : null
            }
        },
        {
            title: '点赞量',
            key: 'likeCount',
            width: 50,
            align: 'center',
            render(row) {
                return row.likeCount === null ? 0 : row.likeCount;
            }
        },
        {
            title: '评论量',
            key: 'commentCount',
            width: 50,
            align: 'center',
            render(row) {
                return row.commentCount === null ? 0 : row.commentCount;
            }
        },
        {
            title: '状态',
            key: 'status',
            width: 70,
            align: 'center',
            render(row) {
                let textInfo: { text: string, type: any };
                switch (row.status) {
                    case 1:
                        textInfo = {text: "公开", type: "success"};
                        break;
                    case 2:
                        textInfo = {text: "私密", type: "info"};
                        break;
                    case 3:
                        textInfo = {text: "草稿", type: "warning"};
                        break;
                    default:
                        textInfo = {text: "未知", type: "error"};
                        break;
                }
                return h(NTag, {type: textInfo.type, bordered: false}, {
                    default: () => textInfo.text
                })
            }
        },
        {
            title: '顶置',
            key: 'isTop',
            width: 60,
            align: 'center',
            render(row) {
                return h(NSwitch, {
                        round: false,
                        loading: row.loading,
                        value: row.isTop,
                        ["onUpdate:value"]: async (value) => {
                            let tipMessage = (window as any).$message.create("保存中...", {
                                type: 'loading'
                            });
                            const form = new FormData()
                            form.append('id', row.id)
                            form.append('isTop', value);
                            row.loading = true
                            await updateTalkTop(form).then((resp) => {
                                if (resp.status) {
                                    row.isTop = value;
                                    tipMessage.type = 'success'
                                    tipMessage.content = resp.message
                                }
                                if (resp.code == StatusCode.FAIL) {
                                    tipMessage.type = 'error'
                                    tipMessage.content = resp.message
                                }
                            }).catch(() => {
                                tipMessage.type = 'error'
                                tipMessage.content = '修改失败'
                            }).finally(() => {
                                row.loading = false
                            })
                        }
                    },
                )
            }
        },
        {
            title: '创建/更新时间',
            key: 'time',
            width: 110,
            align: 'center',
            render(row) {
                return h(NSpace, {vertical: true, style: {fontSize: '12px'}}, {
                    default: () => [
                        h(NPopover, {trigger: "hover"}, {
                            trigger: () => h(NText, {depth: 3}, {
                                default: () => row.createTime
                            }),
                            default: () => "创建时间"
                        }),
                        h(NPopover, {trigger: "hover", placement: "bottom"}, {
                            trigger: () => h(NText, {depth: 3}, {
                                default: () => row.updateTime
                            }),
                            default: () => "更新时间"
                        })
                    ]
                })
            }
        },
        {
            title: '操作',
            key: 'options',
            width: 100,
            align: 'center',
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(NSpace, {justify: 'center'}, {
                    default: () => [
                        h(NPopconfirm, {onPositiveClick: deleteTalkHandle}, {
                            trigger: () => h(NButton, {
                                size: 'small', tertiary: true, type: 'error', onClick: () => {
                                    deleteId.value = row.id
                                }
                            }, {
                                default: () => "删除"
                            }),
                            default: () => "确定删除吗？"
                        })
                        ,
                        h(NButton, {
                            size: 'small', tertiary: true, type: 'info', onClick: () => {
                                // 跳转至说说修改
                                router.replace({path: `/talks/${row.id}`})
                            }
                        }, {
                            default: () => "修改"
                        })
                    ]
                })
            }
        }
    ]
}

const createTalkData = async (condition: ConditionParams) => {
    talkBackData.length = 0
    loading.value = true
    await listTalkBack(condition).then((resp: PageResult<Array<TalkBackModel>>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status && resp.data.recordList !== null) {
            resp.data.recordList.map((talk: TalkBackModel) => {
                talkBackData.push({
                    user: talk.user,
                    id: talk.id,
                    content: talk.content,
                    isTop: talk.isTop,
                    status: talk.status,
                    pictureVideos: talk.pictureVideos,
                    likeCount: talk.likeCount,
                    commentCount: talk.commentCount,
                    createTime: talk.createTime,
                    updateTime: talk.updateTime,
                    loading: false
                })
            })
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch(() => {
        (window as any).$message.error("获取说说列表失败，请重试！");
    }).finally(() => {
        loading.value = false
    })
}
const columns = createColumns({
    talk(row: TalkBackType) {
    }
})

// 分页处理
const handlePageChange = async (currentPage: number) => {
    await createTalkData({current: currentPage});
}

export {
    talkBackData,
    searchData,
    searchTalkHandle,
    statusHandle,
    columns,
    createTalkData,
    showTalkPictureVideoModal,
    talkPictureVideos,
    loading,
    pagination,
    handlePageChange
}