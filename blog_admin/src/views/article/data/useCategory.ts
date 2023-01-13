import type { SelectOption } from 'naive-ui'
import { searchArticleCategory } from '~/api/requests/Category'
import {articleInfo} from "./useArticle"
import { StatusCode } from '~/api/enum/statusCode'
import {ref} from "vue";

const categoryOptionResp = ref([])
const categoryOptions = ref<SelectOption[]>([])

// 加载状态
const categoryOptionLoading = ref(false)

// 初始化分类选项
const initCategoryOption = async () => {
  categoryOptionLoading.value = true
  categoryOptionResp.value.length = 0
  const categorySearchResponse = await searchArticleCategory()
  if (categorySearchResponse.code === StatusCode.SUCCESS && categorySearchResponse.status && categorySearchResponse.data.length !== 0) {
    categoryOptionResp.value = categorySearchResponse.data as any
    categoryOptions.value = categoryOptionResp.value
  } else {
    categoryOptionResp.value.length = 0
  }
  categoryOptionLoading.value = false
}
// 分类搜索
const categorySearch = async (query: string) => {
  categoryOptionLoading.value = true
  categoryOptions.value = categoryOptionResp.value.filter((category: CategorySearchModel) => ~category.name.search(query))
  // 如果没有找到，则新建
  if (query.length !== 0 && query !== articleInfo.categoryName) {
     categoryOptions.value.push({id: "", name: query});
     // 输入列表中已经存在的会报错，
    let hasCategory = categoryOptions.value.filter((category) => category.name === query);
    if (hasCategory.length >= 2) {
      categoryOptions.value.pop()
    }
  }
  categoryOptionLoading.value = false
}

export {
  categoryOptions,
  categoryOptionLoading,
  initCategoryOption,
  categorySearch
}