<script setup lang='ts'>
import {tableSet, tableSize} from '~/components/common/dataTable'
import {Add} from '@icon-park/vue-next'
import {
  resourcesData,
  columns,
  loading,
  createResourceData,
  resourceForm,
  parentResourceOptions,
  requestMethodOptions,
  searchForm,
  searchResourceHandle,
  showSaveOrUpdateResourceModel,
  showSaveOrUpdateResourceModelHandle,
  saveOrUpdateResourceHandle,
} from './data/useResource'
import {
  NSpace,
  NInputGroupLabel,
  NInputGroup,
  NInput,
  NButton,
  NIcon,
  NDataTable,
  NModal,
  NText,
  NSelect,
  NSwitch
} from "naive-ui"
import DescSetting from "~/components/DescSetting.vue"
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createResourceData({})
})

</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-input-group>
        <n-input placeholder="资源名称" clearable size="small" v-model:value="searchForm.keywords"/>
        <n-button type="primary" size="small" ghost @click="searchResourceHandle">搜索</n-button>
      </n-input-group>
      <n-space>
        <n-button secondary strong size="small" @click="showSaveOrUpdateResourceModelHandle">
          <template #icon>
            <n-icon>
              <Add/>
            </n-icon>
          </template>
          新增资源
        </n-button>
        <data-table-setting/>
      </n-space>
    </n-space>
    <n-space>
      <n-data-table
          :columns="columns"
          :data="resourcesData"
          :striped="tableSet.striped"
          :single-line="tableSet.singleLine"
          :size="tableSize"
          size="tableSize"
          :scroll-x="900"
          remote
          :loading="loading"
          :row-key="(row: resourcesData) => row.id"
      />
    </n-space>
    <n-space vertical>
      <n-modal v-model:show="showSaveOrUpdateResourceModel" preset="dialog" title="Dialog" :mask-closable="false"
               :show-icon="false">
        <template #header>
          <n-text v-if="!resourceForm.id">新增资源</n-text>
          <n-text v-else>修改资源</n-text>
        </template>
        <n-space vertical>
          <n-input-group>
            <n-input-group-label>资源名</n-input-group-label>
            <n-input v-model:value="resourceForm.name" placeholder="菜单标题"
                     :status="resourceForm.name.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>父资源</n-input-group-label>
            <n-select v-model:value="resourceForm.parentId" clearable :default-value="resourceForm.parentId"
                      :options="parentResourceOptions" placeholder="父资源，为空则当前为父资源"/>
          </n-input-group>
          <n-input-group v-show="resourceForm.parentId">
            <n-input-group-label>请求方式</n-input-group-label>
            <n-select v-model:value="resourceForm.requestMethod" clearable :default-value="resourceForm.requestMethod"
                      :options="requestMethodOptions"/>
          </n-input-group>
          <n-input-group v-show="resourceForm.parentId">
            <n-input-group-label>请求路径</n-input-group-label>
            <n-input v-model:value="resourceForm.url"/>
          </n-input-group>
          <desc-setting title="状态">
            <n-switch :round="false" v-model:value="resourceForm.isEnable">
              <template #checked>启用</template>
              <template #unchecked>禁用</template>
            </n-switch>
          </desc-setting>
          <desc-setting title="匿名">
            <n-switch :round="false" v-model:value="resourceForm.isAnonymous">
              <template #checked>是</template>
              <template #unchecked>否</template>
            </n-switch>
          </desc-setting>
        </n-space>
        <template #action>
          <n-button :disabled="resourceForm.name.length === 0" tertiary type="warning" size="small"
                    @click="saveOrUpdateResourceHandle">保存
          </n-button>
        </template>
      </n-modal>
    </n-space>
  </n-space>
</template>

<style scoped>
</style>