<script setup lang='ts'>
import {Add, Edit, DeleteFive} from '@icon-park/vue-next'
import {
  roleData,
  defaultTabValue,
  createRoleData,
  roleResourceData,
  createRoleResourceData,
  roleMenuData,
  createRoleMenuData,
  changeTabHandle,
  updateResourceCheckedKeys,
  updateMenuCheckedKeys,
  updateResourceHandle,
  updateMenuHandle,
  showRoleInfoModal,
  showRoleInfoModalHandle,
  updateRoleInfoHandle,
  roleInfoForm,
  checkRoleInfoFormStatusComputed,
  showSaveNewRoleModal,
  showSaveNewRoleModalHandle,
  saveNewRoleHandle,
  deleteRoleHandle
} from './data/useRole'
import {
  NSpace,
  NTabs,
  NTabPane,
  NCard,
  NTree,
  NButton,
  NDivider,
  NIcon,
  NSpin,
  NText,
  NInput,
  NSwitch,
  NModal
} from "naive-ui"
import {onMounted} from "vue";

onMounted(() => {
  createRoleData({current: 1})
  createRoleResourceData()
  createRoleMenuData()
})
</script>

<template>
  <n-space vertical>
    <n-space vertical v-if="roleData.length !== 0">
      <n-tabs type="segment" animated :default-value="defaultTabValue" @update:value="changeTabHandle">
        <n-tab-pane v-for="role in roleData" :key="role.id" :name="role.id" :tab="role.roleDesc">
          <n-card title=" " size="small">
            <n-tabs animated default-value="resource" justify-content="space-evenly" type="line">
              <n-tab-pane name="resource" tab="权限选项">
                <n-tree
                    block-line
                    cascade
                    checkable
                    key-field="id"
                    check-on-click
                    :data="roleResourceData"
                    :default-checked-keys="role.resourceIdList"
                    @update:checked-keys="updateResourceCheckedKeys"
                />
                <n-space justify="end">
                  <n-button strong secondary type="error" @click="updateResourceHandle(role)">
                    更新权限
                  </n-button>
                </n-space>
                <n-divider/>
              </n-tab-pane>
              <n-tab-pane name="menu" tab="菜单选项">
                <n-tree
                    block-line
                    cascade
                    checkable
                    key-field="id"
                    :data="roleMenuData"
                    :default-checked-keys="role.menuIdList"
                    @update:checked-keys="updateMenuCheckedKeys"
                />
                <n-space justify="end">
                  <n-button strong secondary type="error" @click="updateMenuHandle(role)">更新菜单</n-button>
                </n-space>
                <n-divider/>
              </n-tab-pane>
            </n-tabs>
            <template #footer>
              <n-space justify="space-between">
                <n-space>
                  <n-button size="small" type="warning" tertiary @click="showRoleInfoModalHandle(role)">
                    <template #icon>
                      <n-icon>
                        <Edit/>
                      </n-icon>
                    </template>
                    修改角色
                  </n-button>
                  <n-button size="small" type="info" tertiary @click="showSaveNewRoleModalHandle">
                    <template #icon>
                      <n-icon>
                        <Add/>
                      </n-icon>
                    </template>
                    新增角色
                  </n-button>
                </n-space>
                <n-button size="small" type="error" tertiary @click="deleteRoleHandle(role)">
                  <template #icon>
                    <n-icon>
                      <DeleteFive/>
                    </n-icon>
                  </template>
                  删除角色
                </n-button>
              </n-space>
            </template>
          </n-card>
        </n-tab-pane>
      </n-tabs>
    </n-space>
    <n-space v-else vertical justify="center" align="center">
      <n-spin size="large">
        <template #description>
          拼命加载中...
        </template>
      </n-spin>
    </n-space>
    <n-space vertical>
      <n-modal type="warning" v-model:show="showRoleInfoModal" preset="dialog" title="Dialog" :mask-closable=false>
        <template #header>
          <n-text>{{ `修改${roleInfoForm.roleDesc}信息` }}</n-text>
        </template>
        <n-space vertical>
          <n-input v-model:value="roleInfoForm.roleDesc"
                   :status="roleInfoForm.roleDesc.length === 0 ? 'error':'success'"
                   placeholder="角色名称"/>
          <n-input v-model:value="roleInfoForm.roleAuth"
                   :status="roleInfoForm.roleAuth.length === 0 ? 'error':'success'"
                   placeholder="角色权限名"/>
          <n-switch :round="false" v-model:value="roleInfoForm.isEnable">
            <template #checked>
              启用
            </template>
            <template #unchecked>
              关闭
            </template>
          </n-switch>
        </n-space>
        <template #action>
          <n-button :disabled="checkRoleInfoFormStatusComputed" size="small" type="info" tertiary
                    @click="updateRoleInfoHandle">保存
          </n-button>
        </template>
      </n-modal>
      <n-modal v-model:show="showSaveNewRoleModal" preset="dialog" title="Dialog" :mask-closable=false>
        <template #header>
          <n-text>新增角色</n-text>
        </template>
        <n-space vertical>
          <n-input v-model:value="roleInfoForm.roleDesc" placeholder="角色名称"/>
          <n-input v-model:value="roleInfoForm.roleAuth" placeholder="角色权限名"/>
          <n-switch :round="false" v-model:value="roleInfoForm.isEnable">
            <template #checked>
              启用
            </template>
            <template #unchecked>
              关闭
            </template>
          </n-switch>
        </n-space>
        <template #action>
          <n-button size="small" type="info" tertiary @click="saveNewRoleHandle">保存</n-button>
        </template>
      </n-modal>
    </n-space>
  </n-space>
</template>

<style scoped>
</style>