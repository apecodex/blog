<script setup lang='ts'>
import {reviewOptions} from '~/components/common/constant'
import {
  searchData,
  typeHandle,
  reviewHandle,
  columns,
  messageData,
  createMessageData,
  handleCheck,
  checkedRowKeys,
  updateCheckedReview,
  deleteChecked,
  pagination,
  handlePageChange,
  loading
} from './data/useMessage'
import {
  NSpace,
  NSelect,
  NP,
  NButton,
  NPopconfirm,
  NDataTable,
} from "naive-ui"
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {tableSize, tableSet} from "~/components/common/dataTable";
import {onMounted} from "vue";

onMounted(() => {
  createMessageData({current: 1})
})

</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-space align="center">
        <n-select @update:value="typeHandle" size="small"
                  :options="[{label: '用户', value: 1}, {label: '游客', value: 0}]" clearable
                  placeholder="用户类型" :style="{width: '100px'}" v-model:value="searchData.type"/>
        <n-select @update:value="reviewHandle" size="small"
                  :options="reviewOptions" clearable
                  placeholder="审核状态" :style="{width: '100px'}" v-model:value="searchData.isDelete"/>
      </n-space>
      <data-table-setting/>
    </n-space>
    <n-space vertical>
      <n-space align="baseLine">
        <n-p>你选中了 {{ checkedRowKeys.length }} 行。</n-p>
        <n-button v-show="checkedRowKeys.length !== 0" tertiary type="info" size="tiny"
                  @click="updateCheckedReview(false)">审核
        </n-button>
        <n-button v-show="checkedRowKeys.length !== 0" tertiary type="warning" size="tiny"
                  @click="updateCheckedReview(true)">设为待审核
        </n-button>
        <n-popconfirm
            @positive-click="deleteChecked"
        >
          <template #trigger>
            <n-button v-show="checkedRowKeys.length !== 0" tertiary type="error" size="tiny">删除</n-button>
          </template>
          确定删除吗？
        </n-popconfirm>
      </n-space>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="messageData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="1500"
        remote
        :single-column="tableSet.singleColumn"
        :pagination="pagination"
        :row-key="(row: messageData) => row.id"
        :loading="loading"
        :size="tableSize"
        @update:page="handlePageChange"
        @update:checked-row-keys="handleCheck"
    />
  </n-space>
</template>

<style scoped>
</style>