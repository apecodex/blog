<script setup lang='ts'>
import {tableSize, tableSet} from "~/components/common/dataTable";
import {mailTypeOptions} from '~/components/common/constant'
import {
  mailLogData,
  columns,
  createMailLogData,
  checkedRowKeys,
  pagination,
  loading,
  handlePageChange,
  handleCheck,
  deleteCheckedMailLog,
  searchData,
  typeHandle
} from './data/useMailLog'
import {
  NSpace,
  NSelect,
  NP,
  NButton,
  NDataTable
} from "naive-ui";
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createMailLogData({current: 1})
})
</script>

<template>
  <n-space vertical>
    <n-space vertical>
      <n-space justify="space-between" align="baseLine">
        <n-select @update:value="typeHandle" size="small" :options="mailTypeOptions" clearable
                  placeholder="邮件类型" :style="{width: '100px'}" v-model:value="searchData.type"/>
        <data-table-setting/>
      </n-space>
      <n-space align="baseLine">
        <n-p> 你选中了 {{ checkedRowKeys.length }} 行。</n-p>
        <n-button v-show="checkedRowKeys.length !== 0" tertiary type="error" size="tiny"
                  @click="deleteCheckedMailLog">删除
        </n-button>
      </n-space>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="mailLogData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="1100"
        remote
        :single-column="tableSet.singleColumn"
        :row-key="(row: mailLogData) => row.msgId"
        :size="tableSize"
        :loading="loading"
        :pagination="pagination"
        @update:page="handlePageChange"
        @update:checked-row-keys="handleCheck"
    />
  </n-space>
</template>

<style scoped>
</style>