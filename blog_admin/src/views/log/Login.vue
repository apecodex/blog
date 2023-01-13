<script setup lang='ts'>
import {tableSize, tableSet} from "~/components/common/dataTable";
import {
  loginLogData,
  columns,
  showLoginRectangleMap,
  pagination,
  loading,
  checkedRowKeys,
  handlePageChange,
  createLoginLogData,
  handleCheck,
  deleteCheckedLoginLog
} from './data/useLoginLog'
import {
  NSpace,
  NP,
  NButton,
  NDataTable,
  NModal
} from "naive-ui";
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createLoginLogData({current: 1})
})
</script>

<template>
  <n-space vertical>
    <n-space justify="space-between" align="baseLine">
      <n-space align="baseLine">
        <n-p> 你选中了 {{ checkedRowKeys.length }} 行。</n-p>
        <n-button v-show="checkedRowKeys.length !== 0" tertiary type="error" size="tiny"
                  @click="deleteCheckedLoginLog">删除
        </n-button>
      </n-space>
      <data-table-setting/>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="loginLogData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="1300"
        remote
        :single-column="tableSet.singleColumn"
        :row-key="(row: loginLogData) => row.id"
        :size="tableSize"
        :loading="loading"
        :pagination="pagination"
        @update:page="handlePageChange"
        @update:checked-row-keys="handleCheck"
    />
    <n-space vertical>
      <n-modal v-model:show="showLoginRectangleMap">
        <div id="LoginRectangleMap" class="w-500px h-500px rounded-8px"></div>
      </n-modal>
    </n-space>
  </n-space>
</template>

<style scoped>
</style>