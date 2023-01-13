<script setup lang='ts'>
import {
  tagName,
  newTagName,
  searchTagHandle,
  saveTagHandle,
  columns,
  tagsData,
  createTagsData,
  pagination,
  loading,
} from './data/useTag'
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
import {tableSize, tableSet} from "~/components/common/dataTable";
import {Add} from '@icon-park/vue-next'
import {onMounted} from "vue";

onMounted(() => {
  createTagsData({current: 1})
})

</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-space align="center">
        <n-input-group>
          <n-input placeholder="标签名" clearable size="small" v-model:value="tagName"/>
          <n-button type="primary" size="small" ghost @click="searchTagHandle(null)">搜索</n-button>
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
              新增标签
            </n-button>
          </template>
          <template #action>
            <n-button @click="saveTagHandle" size="tiny" quaternary type="primary">保存</n-button>
          </template>
          <n-input type="text" clearable size="small" v-model:value="newTagName" placeholder="标签名"/>
        </n-popconfirm>
        <data-table-setting/>
      </n-space>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="tagsData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        remote
        :scroll-x="800"
        :single-column="tableSet.singleColumn"
        :pagination="pagination"
        :row-key="(row: tagsData) => row.id"
        :loading="loading"
        :size="tableSize"
    />
  </n-space>
</template>

<style scoped>
</style>