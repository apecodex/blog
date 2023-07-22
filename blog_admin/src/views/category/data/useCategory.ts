import type { DataTableColumns } from 'naive-ui'
import type { Ref } from 'vue'
import { h, reactive, ref } from 'vue'
import { NButton, NInput, NPopconfirm, NText } from 'naive-ui'
import { saveOrUpdateCategory, listCategoryBack, deleteCategory } from "~/api/requests/Category";
import { StatusCode } from "~/api/enum/statusCode";
import { isMobile } from "~/utils/utils";

// 加载
const loading = ref(false)
// 搜索的分类关键字
const categoryName = ref("");
// 新增分类名称
const newCategoryName = ref("")
// 删除分类的id
const deleteId = ref("");
// 分类数据
const categoryData: Ref<Array<CategoryBackModel>> = ref<Array<CategoryBackModel>>([])

// 分类分页
const pagination = reactive({
  page: 1,
  pageCount: 1,
  pageSize: 10,
  prefix({ itemCount }: any) {
    return `共有 ${itemCount} 条`
  }
})

// 搜索分类
const searchCategoryHandle = async (current?: number) => {
  const data: ConditionParams = {}
  if (categoryName.value.length !== 0) {
    data.keywords = categoryName.value;
  }
  if (current) {
    data.current = current;
  }
  await createCategoryBackData(data);
}

// 新增分类
const saveCategoryHandle = async () => {
  if (newCategoryName.value.length !== 0) {
    let tipMessage = (window as any).$message.create("添加中...", {
      type: 'loading'
    });
    await saveOrUpdateCategory({ name: newCategoryName.value }).then((resp: ResultObject<null>) => {
      tipMessage.type = 'success'
      tipMessage.content = resp.message
      createCategoryBackData({ current: pagination.page })
    }).catch(() => {
      tipMessage.type = 'error'
      tipMessage.content = '添加失败'
    }).finally(() => {
      newCategoryName.value = ""
    })
  }
}
const deleteCategoryHandle = async () => {
  let tipMessage = (window as any).$message.create("正在删除...", {
    type: 'loading'
  });
  await deleteCategory(deleteId.value).then((resp: ResultObject<null>) => {
    if (resp.status) {
      createCategoryBackData({ current: pagination.page })
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
    tipMessage.content = '删除失败'
  })
}
// 表格表头数据
const createColumns = ({ category }: { category: (row: CategoryBackModel) => void }): DataTableColumns<CategoryBackModel> => {
  return [
    {
      title: '分类名称',
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
              await saveOrUpdateCategory({ categoryId: row.id, name: value as string }).then((resp) => {
                if (resp.status) {
                  row.name = value as string;
                  tipMessage.type = 'success'
                  tipMessage.content = resp.message
                }
                if (resp.code === StatusCode.FAIL) {
                  tipMessage.type = 'warning'
                  tipMessage.content = resp.message
                }
              }).catch(() => {
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
        return h(NText, { depth: 3 }, {
          default: () => row.createTime
        })
      }
    },
    {
      title: '更新时间',
      key: 'updateTime',
      align: 'center',
      width: 180,
      ellipsis: {
        tooltip: true,
      },
      render(row) {
        return h(NText, { depth: 3 }, {
          default: () => row.updateTime
        })
      }
    },
    {
      title: '操作',
      key: 'options',
      align: 'center',
      width: 100,
      fixed: (isMobile() ? false : 'right') as any,
      render(row) {
        return h(NPopconfirm, { onPositiveClick: deleteCategoryHandle }, {
          trigger: () => h(NButton, {
            size: 'small', tertiary: true, type: 'error', onClick() {
              deleteId.value = row.id
            }
          }, { default: () => "删除" }),
          default: () => "确定删除吗？"
        })
      }
    }
  ]
}
// 请求分类数据
const createCategoryBackData = async (condition: ConditionParams) => {
  loading.value = true
  categoryData.value.length = 0
  await listCategoryBack(condition).then((resp: PageResult<Array<CategoryBackModel>>) => {
    if (resp.code === StatusCode.SUCCESS && resp.status) {
      categoryData.value = Array.from(resp.data.recordList)
      pagination.pageCount = Math.trunc((resp.data.count + pagination.pageSize - 1) / pagination.pageSize)
      pagination.page = condition.current as number
      // @ts-ignore
      pagination.itemCount = resp.data.count
    }
  }).catch(() => {
    (window as any).$message.error("获取分类列表错误，请重试！");
  }).finally(() => {
    loading.value = false
  })
}

const columns = createColumns({
  category(row: CategoryBackModel) {
  }
})

// 分页
const handlePageChange = async (currentPage: number) => {
  await createCategoryBackData({ current: currentPage });
}
export {
  loading,
  columns,
  categoryData,
  createCategoryBackData,
  pagination,
  handlePageChange,
  categoryName,
  searchCategoryHandle,
  newCategoryName,
  saveCategoryHandle
}