<script setup lang='ts'>
import {tableSize, tableSet} from "~/components/common/dataTable";
import {reviewOptions} from '~/components/common/constant'
import {Add} from '@icon-park/vue-next'
import {
  friendLinkData,
  columns,
  loading,
  createFriendLinkData,
  pagination,
  handleCheck,
  checkedRowKeys,
  searchData,
  searchFriendLinkHandle,
  reviewHandle,
  updateCheckedReview,
  showSaveOrUpdateFriendLinkModal,
  saveFriendLinkHandle,
  friendLinkForm,
  checkFriendLinkFormComputed,
  saveOrUpdateFriendLinkHandle
} from './data/useFriendLink'
import {
  NSpace,
  NInputGroup,
  NInputGroupLabel,
  NInput,
  NButton,
  NSelect,
  NIcon,
  NP,
  NDataTable,
  NModal,
  NText,
  NSwitch,
} from "naive-ui";
import DescSetting from "~/components/DescSetting.vue"
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createFriendLinkData({current: 1})
})
</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-space align="baseLine">
        <n-input-group>
          <n-input placeholder="友链标题" clearable size="small" v-model:value="searchData.keywords"/>
          <n-button type="primary" size="small" ghost @click="searchFriendLinkHandle">搜索</n-button>
        </n-input-group>
        <n-select size="small"
                  :options="reviewOptions" clearable
                  @update:value="reviewHandle"
                  placeholder="审核状态" :style="{width: '100px'}" v-model:value="searchData.isReview"/>
      </n-space>
      <n-space align="baseLine">
        <n-button secondary strong size="small" @click="saveFriendLinkHandle">
          <template #icon>
            <n-icon>
              <Add/>
            </n-icon>
          </template>
          新增友链
        </n-button>
        <data-table-setting/>
      </n-space>
    </n-space>
    <n-space align="baseLine">
      <n-p>你选中了 {{ checkedRowKeys.length }} 行。</n-p>
      <n-button v-show="checkedRowKeys.length !== 0" tertiary type="info" size="tiny"
                @click="updateCheckedReview(false)">
        审核
      </n-button>
      <n-button v-show="checkedRowKeys.length !== 0" tertiary type="warning" size="tiny"
                @click="updateCheckedReview(true)">
        设为待审核
      </n-button>
    </n-space>
    <n-data-table
        id="friendLink-scroll-container"
        :columns="columns"
        :data="friendLinkData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="1300"
        remote
        :single-column="tableSet.singleColumn"
        :size="tableSize"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: friendLinkData) => row.id"
        @update:checked-row-keys="handleCheck"
    />
    <n-space vertical>
      <n-modal v-model:show="showSaveOrUpdateFriendLinkModal" preset="dialog" title="Dialog" :mask-closable="false"
               :show-icon="false">
        <template #header>
          <n-text v-if="friendLinkForm.id">修改友链</n-text>
          <n-text v-else>新增友链</n-text>
        </template>
        <n-space vertical>
          <n-input-group>
            <n-input-group-label>友链名称</n-input-group-label>
            <n-input v-model:value="friendLinkForm.linkName" placeholder="网站的名称"
                     :status="friendLinkForm.linkName.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>友链头像</n-input-group-label>
            <n-input v-model:value="friendLinkForm.linkAvatar" placeholder="网站的图标或头像"
                     :status="friendLinkForm.linkAvatar.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>网站地址</n-input-group-label>
            <n-input v-model:value="friendLinkForm.linkUrl" placeholder="url地址"
                     :status="friendLinkForm.linkUrl.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>友链简介</n-input-group-label>
            <n-input v-model:value="friendLinkForm.linkIntro" placeholder="网站的故事..."/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>备注</n-input-group-label>
            <n-input v-model:value="friendLinkForm.remark" placeholder="备注"/>
          </n-input-group>
          <desc-setting title="审核">
            <n-switch :round="false" v-model:value="friendLinkForm.isReview">
              <template #checked>
                正常
              </template>
              <template #unchecked>
                待审
              </template>
            </n-switch>
          </desc-setting>
        </n-space>
        <template #action>
          <n-button :disabled="!checkFriendLinkFormComputed" tertiary strong size="small"
                    @click="saveOrUpdateFriendLinkHandle">保存
          </n-button>
        </template>
      </n-modal>
    </n-space>
  </n-space>
</template>

<style scoped>
</style>