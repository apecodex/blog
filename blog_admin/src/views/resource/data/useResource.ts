import type {DataTableColumns, SelectOption} from 'naive-ui'
import type {Ref} from 'vue'
import {NButton, NPopover, NSpace, NTag, NText} from 'naive-ui'
import {h, reactive, ref} from "vue";
import {isMobile} from "~/utils/utils";
import {listResourceBack, saveOrUpdateResource, deleteResource} from '~/api/requests/Resource'
import {StatusCode} from "~/api/enum/statusCode";

const resourcesData: Ref<Array<ResourceBackModel>> = ref<Array<ResourceBackModel>>([])
const loading = ref(false)
const showSaveOrUpdateResourceModel = ref(false)
// 父资源选项
const parentResourceOptions: Ref<Array<SelectOption>> = ref<Array<SelectOption>>([])
// 搜索表单
const searchForm: ConditionParams = reactive({})
// 请求方式选项
const requestMethodOptions: Array<SelectOption> = [
    {
        label: 'GET',
        value: "GET"
    },
    {
        label: 'POST',
        value: "POST"
    },
    {
        label: 'PUT',
        value: "PUT"
    },
    {
        label: 'DELETE',
        value: "DELETE"
    },
]

// 搜索资源
const searchResourceHandle = async () => {
    resourcesData.value.length = 0
    await createResourceData({keywords: searchForm.keywords})
}

const resourceForm: ResourceParams = reactive({
    id: null,
    isAnonymous: false,
    isEnable: false,
    name: '',
    parentId: null,
    requestMethod: null,
    url: null
})

const createResourceData = async (condition: ConditionParams) => {
    loading.value = true
    resourcesData.value.length = 0
    await listResourceBack(condition).then((resp: ResultObject<Array<ResourceBackModel>>) => {
        if (resp.status) {
            resourcesData.value = Array.from(resp.data)
        }
    }).catch(() => {
        (<any>window).$message.error("获取资源列表失败，请重试");
    }).finally(() => {
        loading.value = false
    })
}

const createColumns = ({resource}: { resource: (row: ResourceBackModel) => void }): DataTableColumns<ResourceBackModel> => {
    return [
        {
            title: '名称',
            key: 'name',
            width: 180,
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const type = row.url ? 'info' : 'success'
                return h(NText, {type}, {
                    default: () => row.name
                })
            }
        },
        {
            title: '路径',
            key: 'url',
            width: 160,
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                return h(NText, {type: 'warning'}, {
                    default: () => row.url
                })
            }
        }
        ,
        {
            title: '请求方式',
            key: 'requestMethod',
            width: 90,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const type = row.requestMethod === "GET" ? 'success' : row.requestMethod === "POST" ? 'info' : row.requestMethod === "PUT" ? 'warning' : 'error'
                return row.requestMethod ? h(NTag, {type, bordered: false}, {
                    default: () => row.requestMethod
                }) : null
            }
        },
        {
            title: '状态',
            key: 'isEnable',
            width: 70,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const type = row.isEnable ? 'success' : 'error'
                return h(NTag, {type}, {
                    default: () => row.isEnable ? '正常' : '禁用'
                })
            }
        },
        {
            title: '匿名访问',
            key: 'isAnonymous',
            width: 80,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            render(row) {
                const type = row.isAnonymous ? 'success' : 'error'
                return h(NTag, {type, bordered: false}, {
                    default: () => row.isAnonymous ? '是' : '否'
                })
            }
        },
        {
            title: '创建/更新时间',
            key: 'time',
            width: 150,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
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
            width: 130,
            align: 'center',
            ellipsis: {
                tooltip: true,
            },
            fixed: (isMobile() ? false : 'right') as any,
            render(row) {
                return h(NSpace, {justify: 'center'}, {
                    default: () => [
                        h(NButton, {
                            size: 'small',
                            type: 'info',
                            tertiary: true,
                            onClick: () => {
                                resourceForm.id = row.id
                                resourceForm.isAnonymous = row.isAnonymous
                                resourceForm.isEnable = row.isEnable
                                resourceForm.name = row.name
                                resourceForm.parentId = row.parentId
                                resourceForm.requestMethod = row.requestMethod
                                resourceForm.url = row.url
                                parentResourceOptions.value.length = 0
                                const parentResource = resourcesData.value.filter((resource) => resource.parentId === null && resource.id !== row.id).map((r) => {
                                    return {label: r.name, value: r.id}
                                });
                                parentResourceOptions.value = Array.from(parentResource)
                                showSaveOrUpdateResourceModel.value = true
                            }
                        }, {
                            default: () => '修改'
                        }),
                        h(NButton, {
                            size: 'small',
                            type: 'error',
                            tertiary: true,
                            onClick: () => {
                                (<any>window).$dialog.warning({
                                    title: '警告',
                                    content: `你确定删除'${row.name}'吗？`,
                                    positiveText: '确定',
                                    negativeText: '算了',
                                    onPositiveClick: async () => {
                                        let tipMessage = (<any>window).$message.create('正在删除...', {
                                            type: 'loading'
                                        })
                                        await deleteResource(row.id).then((resp: ResultObject<null>) => {
                                            if (resp.status) {
                                                resourcesData.value.length = 0
                                                createResourceData({})
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
                                })
                            }
                        }, {
                            default: () => '删除'
                        }),
                    ]
                })
            }
        }
    ]
}

const columns = createColumns({
    resource(row: ResourceBackModel) {
    }
})

// 组装form数据
const assembleResourceFormData = (): FormData => {
    const form = new FormData()
    form.append('id', resourceForm.id ? resourceForm.id : '')
    form.append('isAnonymous', resourceForm.isAnonymous as any)
    form.append('isEnable', resourceForm.isEnable as any)
    form.append('name', resourceForm.name)
    form.append('parentId', resourceForm.parentId ? resourceForm.parentId : '')
    form.append('requestMethod', resourceForm.requestMethod ? resourceForm.requestMethod : '')
    form.append('url', resourceForm.url ? resourceForm.url : '')
    return form;
}

// 新增时处理
const showSaveOrUpdateResourceModelHandle = () => {
    resourceForm.id = null
    resourceForm.isAnonymous = false
    resourceForm.isEnable = false
    resourceForm.name = ''
    resourceForm.parentId = null
    resourceForm.requestMethod = null
    resourceForm.url = null
    parentResourceOptions.value.length = 0
    const parentResource = resourcesData.value.filter((resource: ResourceBackModel) => resource.parentId === null).map((r) => {
        return {label: r.name, value: r.id}
    });
    parentResourceOptions.value = Array.from(parentResource)
    showSaveOrUpdateResourceModel.value = true
}

// 新增或修改资源
const saveOrUpdateResourceHandle = async () => {
    let tipMessage = (<any>window).$message.create('保存中...', {
        type: 'loading'
    })
    if (resourceForm.name.length === 0) {
        tipMessage.type = 'warning'
        tipMessage.content = '资源名称不能为空'
        return;
    }
    const form = assembleResourceFormData();
    await saveOrUpdateResource(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            showSaveOrUpdateResourceModel.value = false
            resourcesData.value.length = 0
            createResourceData({})
            tipMessage.type = 'success'
            tipMessage.content = resp.message
        }
    }).catch(() => {
        tipMessage.type = 'error'
        tipMessage.content = '保存失败'
    })
}
export {
    resourcesData,
    columns,
    loading,
    createResourceData,
    resourceForm,
    parentResourceOptions,
    requestMethodOptions,
    searchForm,
    searchResourceHandle,
    showSaveOrUpdateResourceModel,
    showSaveOrUpdateResourceModelHandle,
    saveOrUpdateResourceHandle,
}