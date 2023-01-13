<script setup lang='ts'>
import {tableSize, tableSet} from "~/components/common/dataTable";

import {
  columns,
  usersData,
  searchData,
  searchUserHandle,
  createUsersData,
  pagination,
  loading,
  handlePageChange,
  showMapModal,
  showRoleOptionModal,
  showSendNoticeModal,
  roleOptions,
  roleOptionLoading,
  saveUserRoles,
  sendNoticeForm,
  sendNoticeHandle,
} from './data/useUsers'
import {
  NSpace,
  NInputGroup,
    NInput,
    NButton,
    NDataTable,
    NModal,
    NSpin,
    NCheckbox
} from "naive-ui";
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createUsersData({current: 1})
})

</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-input-group>
        <n-input placeholder="用户昵称/邮箱" clearable size="small" v-model:value="searchData.keywords"/>
        <n-button type="primary" size="small" ghost @click="searchUserHandle">搜索</n-button>
      </n-input-group>
      <data-table-setting/>
    </n-space>
    <n-data-table
        id="userList-scroll-container"
        :columns="columns"
        :data="usersData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :scroll-x="2690"
        remote
        :loading="loading"
        :pagination="pagination"
        :single-column="tableSet.singleColumn"
        :row-key="(row: usersData) => row.id"
        :size="tableSize"
        @update:page="handlePageChange"
    />
    <n-modal v-model:show="showMapModal">
      <div id="LastLoginMap" class="w-500px h-500px rounded-8px"></div>
    </n-modal>
    <n-modal v-model:show="showRoleOptionModal" preset="dialog" title="Dialog">
      <template #header>
        <div>用户权限修改</div>
      </template>
      <n-space vertical>
        <n-spin :show="roleOptionLoading" size="small">
          <n-checkbox v-for="role in roleOptions.roles" v-model:checked="role.checked" :key="role.id">
            {{ role.roleDesc }}
          </n-checkbox>
        </n-spin>
      </n-space>
      <template #action>
        <n-button tertiary type="info" @click="saveUserRoles">
          保存
        </n-button>
      </template>
    </n-modal>
    <n-modal v-model:show="showSendNoticeModal" preset="dialog" title="Dialog" :show-icon="false">
      <template #header>
        <div>发送通知</div>
      </template>
      <n-space vertical>
        <div>
          <span>通知标题</span>
          <n-input v-model:value="sendNoticeForm.title" type="text" placeholder="标题"/>
        </div>
        <div>
          <span>通知内容</span>
          <n-input v-model:value="sendNoticeForm.content" type="text" placeholder="通知内容"/>
        </div>
      </n-space>
      <template #action>
        <n-button tertiary type="info" @click="sendNoticeHandle">
          保存
        </n-button>
      </template>
    </n-modal>
  </n-space>
</template>

<style scoped>
</style>