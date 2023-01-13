import type {DataTableColumns, SelectOption} from 'naive-ui'
import {h, ref, reactive} from 'vue'
import {NTag, NButton, NAvatar, NSpace, NPopover, NImage, NSpin, NSwitch, NPopconfirm, NText} from 'naive-ui'
import DescSetting from '~/components/DescSetting.vue'
import {DeleteThree, CheckOne} from '@icon-park/vue-next'
import {updateArticleTop, listArticleBack, updateArticleDeleteStatus, deleteArticle} from '~/api/requests/Article'
import {imageFallbackSrc, avatarFallbackSrc} from '~/components/common/constant'
import {router} from '~/router'
import {StatusCode} from "~/api/enum/statusCode";
import {isMobile} from "~/utils/utils";

type ArticleData = {
    id: string
    articleTitle: string
    categoryName: string
    author: SimpleUserInfoModel
    articleCover: string
    tagNames: string[]
    likeCount: number | null
    viewsCount: number | null
    type: number | string
    status: number | string
    isDelete: boolean
    isTop: boolean
    createTime: string,
    updateTime: string,
    loading: boolean
}

// 加载
const loading = ref(false)
// 文章信息列表
const articleBackData: ArticleData[] = reactive([]);
// 每个文章的key
const rowKey = (row: ArticleData) => row.id;
// 文章分页
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
    type: null,
    isDelete: null,
    current: 1,
})

// 搜索文章处理
const searchArticleHandle = async (current?: number) => {
    const data: ConditionParams = {}
    if (searchData.keywords?.length !== 0) {
        data.keywords = searchData.keywords
    }
    if (searchData.status !== null) {
        data.status = searchData.status
    }
    if (searchData.type !== null) {
        data.type = searchData.type
    }
    if (searchData.isDelete !== null) {
        data.isDelete = searchData.isDelete
    }
    if (current) {
        data.current = current
    }
    await createArticleBackListData(data as ConditionParams)
}

// 文章状态处理方法
const statusHandle = async (value: number, option: SelectOption) => {
    searchData.status = value
    await searchArticleHandle()
}
// 文章类型处理方法
const typeHandle = async (value: number, option: SelectOption) => {
    searchData.type = value
    await searchArticleHandle()
}
// 文章删除处理方法
const deleteHandle = async (value: number, option: SelectOption) => {
    searchData.isDelete = value
    await searchArticleHandle()
}

// 表头信息
const createColumns = ({articleInfo}: { articleInfo: (row: ArticleData) => void }): DataTableColumns<ArticleData> => {
    return [
        {
            title: '标题',
            key: 'articleTitle',
            width: 120,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {type: "info"}, {
                    default: () => `《${row.articleTitle}》`
                })
            }

        },
        {
            title: '分类',
            key: 'categoryName',
            width: 80,
            align: 'center',
            ellipsis: {
                tooltip: true
            },
            render(row) {
                return h(NText, {depth: 3}, {
                    default: () => row.categoryName
                })
            }
        },
        {
            title: '作者',
            key: 'author',
            width: 30,
            align: 'center',
            render(row) {
                return h(NPopover, {trigger: 'hover', style: {padding: '5px'}}, {
                    trigger: () => h(NAvatar, {size: 'small', src: row.author.avatar, fallbackSrc: avatarFallbackSrc}),
                    default: () => h(NSpace, {vertical: true, style: {fontSize: '12px'}}, {
                        default: () => [
                            'uid: ' + row.author.uid,
                            '昵称: ' + row.author.nickname
                        ]
                    })
                })
            }
        },
        {
            title: '封面',
            key: 'articleCover',
            width: 60,
            align: 'center',
            render(row) {
                return h(NImage, {
                    lazy: true,
                    intersectionObserverOptions: {root: '#image-scroll-container'},
                    fallbackSrc: imageFallbackSrc,
                    objectFit: "cover",
                    imgProps: {style: {height: "100%", width: '100%'}},
                    src: row.articleCover,
                    class: "w-full h-60px",
                    showToolbarTooltip: true,
                }, {
                    placeholder: () => h(NSpin, {size: 'small'})
                })
            }
        },
        {
            title: '标签',
            key: 'tagNames',
            width: 160,
            align: 'center',
            render(row) {
                const color = ['#40407a', '#706fd3', '#34ace0', '#33d9b2', '#2c2c54', '#474787', '#227093', '#218c74', '#ff5252', '#ff793f', '#ffb142', '#ffda79', '#b33939', '#cd6133', '#cd6133', '#cc8e35', '#ccae62']
                return h(NSpace, {justify: 'center'}, {
                    default: () => {
                        const tags = row.tagNames.map((tagKey) => {
                            return h(
                                NTag,
                                {
                                    size: 'small',
                                    color: {borderColor: color[Math.floor(Math.random() * color.length)]}
                                },
                                {
                                    default: () => tagKey
                                }
                            )
                        })
                        return tags
                    }
                })
            }
        },
        {
            title: '点赞量',
            key: 'likeCount',
            width: 40,
            align: 'center',
            render(row) {
                return row.likeCount === null ? 0 : row.likeCount;
            }
        },
        {
            title: '访问量',
            key: 'viewsCount',
            width: 40,
            align: 'center',
            render(row) {
                return row.viewsCount === null ? 0 : row.viewsCount;
            }
        },
        {
            title: '类型',
            key: 'type',
            width: 50,
            align: 'center',
            render(row) {
                let textInfo: { text: string, type: any };
                switch (row.type) {
                    case 1:
                        textInfo = {text: "原创", type: "success"};
                        break;
                    case 2:
                        textInfo = {text: "转载", type: "info"};
                        break;
                    case 3:
                        textInfo = {text: "翻译", type: "warning"};
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
            title: '状态',
            key: 'status',
            width: 50,
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
            title: '删除',
            key: 'isDelete',
            width: 60,
            align: 'center',
            render(row) {
                return h(NTag, {bordered: false, type: row.isDelete ? 'error' : 'success'}, {
                    icon: () => row.isDelete ? h(DeleteThree, {theme: 'outline'}) : h(CheckOne, {theme: 'outline'}),
                    default: () => row.isDelete ? "删除" : "正常"
                })
            }
        },
        {
            title: '顶置',
            key: 'isTop',
            width: 50,
            align: 'center',
            render(row) {
                return h(NSwitch, {
                        round: false,
                        loading: row.loading,
                        value: row.isTop,
                        ["onUpdate:value"]: async (value) => {
                            let tipMessage = (window as any).$message.create("保存中...", {
                                type: 'loading'
                            })
                            const form = new FormData()
                            form.append('id', row.id)
                            form.append('isTop', value);
                            row.loading = true
                            await updateArticleTop(form).then((resp) => {
                                if (resp.status) {
                                    row.isTop = value;
                                    tipMessage.type = 'success'
                                    tipMessage.content = resp.message
                                }
                            }).catch((error) => {
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
            width: 100,
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
            key: 'option',
            width: 68,
            align: 'center',
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(NSpace, {justify: 'center'}, {
                    default: () => [
                        [
                            h(NPopconfirm, null, {
                                trigger: () => h(
                                    NButton,
                                    {
                                        size: 'small',
                                        tertiary: true,
                                        type: 'error',
                                    },
                                    {default: () => '删除'}
                                ),
                                default: () => "要考虑清楚哦？",
                                action: () => {
                                    return [
                                        row.isDelete ?
                                            h(NButton,
                                                {
                                                    size: 'small',
                                                    quaternary: true,
                                                    type: 'info',
                                                    onClick: async () => {
                                                        let tipMessage = (window as any).$message.create("保存中...", {
                                                            type: 'loading'
                                                        });
                                                        await updateArticleDeleteStatus({
                                                            idList: new Array<string>(row.id),
                                                            isDelete: false
                                                        }).then((resp) => {
                                                            if (resp.status) {
                                                                row.isDelete = false;
                                                                tipMessage.type = 'success'
                                                                tipMessage.content = resp.message
                                                            }
                                                        }).catch((error) => {
                                                            tipMessage.type = 'error'
                                                            tipMessage.content = '恢复失败'
                                                        })
                                                    }
                                                },
                                                {
                                                    default: () => "恢复"
                                                }
                                            ) : h(NButton,
                                                {
                                                    size: 'small',
                                                    quaternary: true,
                                                    type: 'warning',
                                                    onClick: async () => {
                                                        let tipMessage = (window as any).$message.create("保存中...", {
                                                            type: 'loading'
                                                        });
                                                        await updateArticleDeleteStatus({
                                                            idList: new Array<string>(row.id),
                                                            isDelete: true
                                                        }).then((resp) => {
                                                            if (resp.status) {
                                                                row.isDelete = true;
                                                                tipMessage.type = 'success'
                                                                tipMessage.content = resp.message
                                                            }
                                                        }).catch((error) => {
                                                            tipMessage.type = 'error'
                                                            tipMessage.content = '删除失败'
                                                        })
                                                    }
                                                },
                                                {
                                                    default: () => "删除"
                                                }
                                            ),
                                        h(NButton,
                                            {
                                                size: 'small',
                                                quaternary: true,
                                                type: 'error',
                                                onClick: async () => {
                                                    let tipMessage = (window as any).$message.create("正在删除...", {
                                                        type: 'loading'
                                                    });
                                                    await deleteArticle(new Array<string>(row.id)).then((resp: ResultObject<null>) => {
                                                        // 删除后重新查询
                                                        if (resp.status) {
                                                            searchArticleHandle(pagination.page);
                                                            tipMessage.type = 'success'
                                                            tipMessage.content = resp.message
                                                        }
                                                    }).catch((error) => {
                                                        tipMessage.type = 'error'
                                                        tipMessage.content = '删除失败'
                                                    })
                                                }
                                            },
                                            {
                                                default: () => "彻底删除"
                                            }
                                        )
                                    ]
                                }
                            }),
                            h(
                                NButton,
                                {
                                    size: 'small',
                                    tertiary: true,
                                    type: 'info',
                                    onClick: () => {
                                        // 跳转至修改文章页面
                                        router.replace({path: `/articles/${row.id}`})
                                    }
                                },
                                {default: () => '修改'}
                            )
                        ]
                    ]
                })
            }
        },
    ]
}

const columns = createColumns({
    articleInfo(rowData: ArticleData) {
    }
})

const createArticleBackListData = async (condition: ConditionParams) => {
    loading.value = true
    await listArticleBack(condition).then((resp: PageResult<Array<ArticleBackModel>>) => {
        // 清空文章信息
        articleBackData.length = 0
        if (resp.code === StatusCode.SUCCESS && resp.status) {
            resp.data.recordList.map((article: ArticleBackModel) => {
                articleBackData.push({
                    id: article.id,
                    articleTitle: article.articleTitle,
                    categoryName: article.categoryName,
                    author: {
                        uid: article.author.uid,
                        nickname: article.author.nickname,
                        avatar: article.author.avatar,
                        intro: article.author.intro,
                        webSite: article.author.webSite
                    },
                    articleCover: article.articleCover,
                    tagNames: article.tagNames,
                    likeCount: article.likeCount,
                    viewsCount: article.viewsCount,
                    type: article.type,
                    status: article.status,
                    isDelete: article.isDelete,
                    isTop: article.isTop,
                    createTime: article.createTime,
                    updateTime: article.updateTime,
                    loading: false
                })
            })
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch(() => {
        (window as any).$message.error("获取文章列表失败，请重试！");
    }).finally(() => {
        loading.value = false
    })
}

// 分页
const handlePageChange = async (currentPage: number) => {
    await createArticleBackListData({current: currentPage});
}
export {
    statusHandle,
    typeHandle,
    deleteHandle,
    columns,
    articleBackData,
    createArticleBackListData,
    loading,
    rowKey,
    pagination,
    handlePageChange,
    searchData,
    searchArticleHandle
}