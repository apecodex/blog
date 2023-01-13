<script setup lang='ts'>
import {reviewOptions} from '~/components/common/constant'
import {
  searchData,
  searchCommentHandle,
  typeHandle,
  reviewHandle,
  handleCheck,
  checkedRowKeys,
  columns,
  pagination,
  handlePageChange,
  loading,
  commentData,
  createCommentData,
  updateCheckedReview
} from './data/useComment'
import {
  NSpace,
  NInputGroup,
  NInput,
  NButton,
  NSelect,
  NDataTable,
  NP
} from "naive-ui"
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {tableSize, tableSet} from "~/components/common/dataTable";
import {onMounted} from "vue";

onMounted(() => {
  createCommentData({current: 1})
})

</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-space align="center">
        <n-input-group>
          <n-input placeholder="用户名" clearable size="small" v-model:value="searchData.keywords"/>
          <n-button type="primary" size="small" ghost @click="searchCommentHandle(null)">搜索</n-button>
        </n-input-group>
        <n-select @update:value="typeHandle" size="small"
                  :options="[{label: '文章', value: 1}, {label: '说说', value: 2}]" clearable
                  placeholder="评论类型" :style="{width: '100px'}" v-model:value="searchData.type"/>
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
      </n-space>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="commentData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="1580"
        remote
        :single-column="tableSet.singleColumn"
        :pagination="pagination"
        :row-key="(row: commentData) => row.id"
        :loading="loading"
        :size="tableSize"
        @update:page="handlePageChange"
        @update:checked-row-keys="handleCheck"
    />
  </n-space>
</template>

<style scoped>
</style>