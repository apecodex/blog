<script setup lang='ts'>
import {
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
} from './data/useCategory'
import {
  NSpace,
  NInputGroup,
  NInput,
  NButton,
  NPopconfirm,
  NIcon,
  NDataTable
} from "naive-ui"
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {Add} from '@icon-park/vue-next'
import {tableSize, tableSet} from "~/components/common/dataTable";
import {onMounted} from "vue";

onMounted(() => {
  createCategoryBackData({current: 1})
})

</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-space align="center">
        <n-input-group>
          <n-input placeholder="分类名称" clearable size="small" v-model:value="categoryName"/>
          <n-button type="primary" size="small" ghost @click="searchCategoryHandle(null)">搜索</n-button>
        </n-input-group>
      </n-space>
      <n-space>
        <n-popconfirm
            :show-icon="false"
            placement="bottom"
            trigger="click"
        >
          <template #trigger>
            <n-button secondary strong size="small">
              <template #icon>
                <n-icon>
                  <Add/>
                </n-icon>
              </template>
              新增分类
            </n-button>
          </template>
          <template #action>
            <n-button @click="saveCategoryHandle" size="tiny" quaternary type="primary">保存</n-button>
          </template>
          <n-input type="text" clearable size="small" v-model:value="newCategoryName" placeholder="分类名称"/>
        </n-popconfirm>
        <data-table-setting/>
      </n-space>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="categoryData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        remote
        :scroll-x="1000"
        :single-column="tableSet.singleColumn"
        :pagination="pagination"
        :row-key="(row: categoryData) => row.id"
        :loading="loading"
        :size="tableSize"
        @update:page="handlePageChange"
    />
  </n-space>
</template>

<style scoped>
/* 修改分割线margin */
.n-divider:not(.n-divider--vertical) {
  margin-top: 5px !important;
  margin-bottom: 5px !important;
}
</style>