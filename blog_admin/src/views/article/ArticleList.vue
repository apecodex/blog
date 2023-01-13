<script setup lang='ts'>
import {
  statusHandle,
  typeHandle,
  deleteHandle,
  columns,
  articleBackData,
  pagination,
  handlePageChange,
  loading,
  rowKey,
  createArticleBackListData,
  searchData,
  searchArticleHandle
} from './data/useArticleList'
import {statusOptions, typeOptions} from '~/components/common/constant'
import {Repair} from '@icon-park/vue-next'
import {tableSize, tableSet} from "~/components/common/dataTable";
import {
  NSpace,
  NInputGroup,
  NInput,
  NButton,
  NSelect,
  NIcon,
  NDataTable,
} from "naive-ui"
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {useRouter} from "vue-router";
import {onMounted} from "vue";

const router = useRouter();

onMounted(() => {
  createArticleBackListData({current: 1});
})

</script>
<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-space align="center">
        <n-input-group>
          <n-input placeholder="文章标题" clearable size="small" v-model:value="searchData.keywords"/>
          <n-button type="primary" size="small" ghost @click="searchArticleHandle(null)">搜索</n-button>
        </n-input-group>
        <n-select @update:value="statusHandle" size="small" :options="statusOptions" clearable
                  placeholder="文章状态" :style="{width: '100px'}" v-model:value="searchData.status"/>
        <n-select @update:value="typeHandle" size="small" :options="typeOptions" clearable
                  placeholder="文章类型" :style="{width: '100px'}" v-model:value="searchData.type"/>
        <n-select @update:value="deleteHandle" size="small"
                  :options="[{label: '删除', value: 1}, {label: '正常', value: 0}]" clearable
                  placeholder="删除状态" :style="{width: '100px'}" v-model:value="searchData.isDelete"/>
      </n-space>
      <n-space>
        <n-button secondary strong size="small" @click="router.replace({path: '/articles'})">
          <template #icon>
            <n-icon>
              <Repair/>
            </n-icon>
          </template>
          发布文章
        </n-button>
        <data-table-setting/>
      </n-space>
    </n-space>
    <n-data-table id="image-scroll-container"
                  :columns="columns"
                  :data="articleBackData"
                  :striped="tableSet.striped"
                  :single-line="tableSet.singleLine"
                  :scroll-x="1800"
                  remote
                  :single-column="tableSet.singleColumn"
                  :pagination="pagination"
                  :row-key="rowKey"
                  :loading="loading"
                  :size="tableSize"
                  @update:page="handlePageChange"/>
  </n-space>
</template>

<style scoped>
</style>