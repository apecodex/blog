import type {DataTableColumns} from 'naive-ui'
import type {Ref} from 'vue'
import {h, reactive, ref} from 'vue'
import {NButton, NInput, NPopconfirm, NText} from 'naive-ui'
import {updateTag, deleteTag, listTagBack, saveTag} from '~/api/requests/Tag'
import {StatusCode} from "~/api/enum/statusCode";
import {isMobile} from "~/utils/utils";

// 加载
const loading = ref(false)
// 搜索的标签名称
const tagName = ref("")
// 新增标签名称
const newTagName = ref("")
// 后台标签数据
const tagsData: Ref<Array<TagBackModel>> = ref<Array<TagBackModel>>([]);
// 删除id
const deleteId = ref("");
// 分类分页
const pagination = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    prefix({itemCount}: any) {
        return `共有 ${itemCount} 条`
    }
})

// 删除标签的方法
const deleteTagHandle = async () => {
    let tipMessage = (window as any).$message.create("正在删除...", {
        type: 'loading'
    });
    const form = new FormData()
    form.append("tagId", deleteId.value)
    await deleteTag(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            createTagsData({current: pagination.page});
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
}

// 搜索标签的方法
const searchTagHandle = async (current: number) => {
    const data: ConditionParams = {}
    if (tagName.value.length !== 0) {
        data.keywords = tagName.value;
    }
    if (current) {
        data.current = current;
    }
    await createTagsData(data);
}
// 新增标签
const saveTagHandle = async () => {
    let tipMessage = (window as any).$message.create("添加中...", {
        type: 'loading'
    });
    await saveTag(new Array<string>(newTagName.value)).then((resp: ResultObject<null>) => {
        if (resp.status) {
            createTagsData({current: pagination.page});
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '添加失败'
    }).finally(() => {
        newTagName.value = ""
    })
}
// 表头的数据
const createColumns = ({tag}: { tag: (row: TagBackModel) => void }): DataTableColumns<TagBackModel> => {
    return [
        {
            title: '标签名称',
            key: 'name',
            width: 180,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(
                    NInput,
                    {
                        value: row.name,
                        onUpdateValue(v) {
                            row.name = v
                        },
                        async onChange(value: string | [string, string]) {
                            let tipMessage = (window as any).$message.create("保存中...", {
                                type: 'loading'
                            });
                            const form = new FormData()
                            form.append("id", row.id)
                            form.append("name", row.name)
                            await updateTag(form).then((resp: ResultObject<null>) => {
                                    if (resp.status) {
                                        row.name = value as string;
                                        tipMessage.type = 'success'
                                        tipMessage.content = resp.message
                                    }
                                    if (resp.code === StatusCode.FAIL) {
                                        tipMessage.type = 'warning'
                                        tipMessage.content = resp.message
                                    }
                                }
                            ).catch(() => {
                                tipMessage.type = 'error'
                                tipMessage.content = '保存失败'
                            })
                        }
                    })
            }
        },
        {
            title: '文章数量',
            key: 'articleCount',
            align: 'center',
            width: 90,
        },
        {
            title: '创建时间',
            key: 'createTime',
            align: 'center',
            width: 180,
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
            align: 'center',
            width: 80,
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(NPopconfirm, {onPositiveClick: deleteTagHandle}, {
                    trigger: () => h(NButton, {
                        size: 'small', tertiary: true, type: 'error', onClick() {
                            deleteId.value = row.id
                        }
                    }, {default: () => "删除"}),
                    default: () => "确定删除吗？"
                })
            }
        }
    ]
}

const createTagsData = async (condition: ConditionParams) => {
    loading.value = true
    tagsData.value.length = 0
    await listTagBack(condition).then((resp: PageResult<Array<TagBackModel>>) => {
        if (resp.code === StatusCode.SUCCESS && resp.status) {
            tagsData.value = Array.from(resp.data.recordList)
            pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
            pagination.page = condition.current as number
            // @ts-ignore
            pagination.itemCount = resp.data.count
        }
    }).catch((error) => {
        (window as any).$message.error("获取标签列表错误，请重试！");
    }).finally(() => {
        loading.value = false
    })
}

const columns = createColumns({
    tag(row: TagBackModel) {
    }
})

export {
    tagName,
    newTagName,
    searchTagHandle,
    saveTagHandle,
    columns,
    tagsData,
    pagination,
    createTagsData,
    loading
}
