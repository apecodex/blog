<script setup lang='ts'>
import {imageFallbackSrc} from '~/components/common/constant'
import {tableSize, tableSet} from "~/components/common/dataTable";
import {statusOptions} from '~/components/common/constant'
import {Repair} from '@icon-park/vue-next'
import {router} from '~/router'
import {
  talkBackData,
  searchData,
  searchTalkHandle,
  statusHandle,
  columns,
  createTalkData,
  loading,
  pagination,
  handlePageChange,
  showTalkPictureVideoModal,
  talkPictureVideos
} from './data/useTalkList'
import {
  NSpace,
  NInputGroup,
  NInput,
  NButton,
  NSelect,
  NIcon,
  NDataTable,
  NModal,
  NScrollbar,
  NImageGroup,
  NImage,
  NSpin
} from "naive-ui";
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createTalkData({current: 1})
})
</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-space align="center">
        <n-input-group>
          <n-input placeholder="说说内容" clearable size="small" v-model:value="searchData.keywords"/>
          <n-button type="primary" size="small" ghost @click="searchTalkHandle(null)">搜索</n-button>
        </n-input-group>
        <n-select @update:value="statusHandle" size="small" :options="statusOptions" clearable
                  placeholder="说说状态" :style="{width: '100px'}" v-model:value="searchData.status"/>
      </n-space>
      <n-space>
        <n-button secondary strong size="small" @click="router.replace({path: '/talks'})">
          <template #icon>
            <n-icon>
              <Repair/>
            </n-icon>
          </template>
          发布说说
        </n-button>
        <data-table-setting/>
      </n-space>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="talkBackData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="1000"
        remote
        :single-column="tableSet.singleColumn"
        :pagination="pagination"
        :row-key="(row: talkBackData) => row.id"
        :loading="loading"
        :size="tableSize"
        @update:page="handlePageChange"
    />
    <n-modal v-model:show="showTalkPictureVideoModal" preset="card" size="small" :style="{ maxWidth: '600px'}"
             :mask-closable="false">
      <n-scrollbar style="max-height: 300px">
        <n-image-group>
          <n-space id="talkPictureVideo-scroll-container" class="!gap-5px">
            <n-image v-for="file in talkPictureVideos"
                     :key="file.id"
                     width="100"
                     show-toolbar-tooltip
                     object-fit="cover"
                     :style="{height: '100px'}"
                     :src="file.src"
                     lazy
                     :alt="file.fileName"
                     :fallback-src="imageFallbackSrc"
                     :intersection-observer-options="{root: '#talkPictureVideo-scroll-container'}"
            >
              <template #placeholder>
                <div class="w-100px h-100px flex items-center justify-center">
                  <n-spin size="small"/>
                </div>
              </template>
            </n-image>
          </n-space>
        </n-image-group>
      </n-scrollbar>
    </n-modal>
  </n-space>
</template>

<style scoped>
</style>