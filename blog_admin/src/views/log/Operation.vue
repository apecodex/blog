<script setup lang='ts'>
import {tableSize, tableSet} from "~/components/common/dataTable";
import {
  operationData,
  columns,
  pagination,
  loading,
  handlePageChange,
  createOperationData,
  handleCheck,
  checkedRowKeys,
  deleteCheckedOperationLog
} from './data/useOperationLog'
import {
  NSpace,
  NP,
  NButton,
  NDataTable
} from "naive-ui";
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createOperationData({current: 1})
})
</script>

<template>
  <n-space vertical>
    <n-space justify="space-between" align="baseLine">
      <n-space align="baseLine">
        <n-p> 你选中了 {{ checkedRowKeys.length }} 行。</n-p>
        <n-button v-show="checkedRowKeys.length !== 0" tertiary type="error" size="tiny"
                  @click="deleteCheckedOperationLog">删除
        </n-button>
      </n-space>
      <data-table-setting/>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="operationData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="1800"
        remote
        :single-column="tableSet.singleColumn"
        :pagination="pagination"
        :row-key="(row: operationData) => row.id"
        :size="tableSize"
        :loading="loading"
        @update:page="handlePageChange"
        @update:checked-row-keys="handleCheck"
    />
  </n-space>
</template>

<style scoped>
</style>