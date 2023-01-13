<script setup lang='ts'>
import {tableSet, tableSize} from '~/components/common/dataTable'
import {Add} from '@icon-park/vue-next'
import {
  menuData,
  columns,
  createMenuData,
  loading,
  showSaveOrUpdateModel,
  showSaveOrUpdateModelHandle,
  menuForm,
  menuParentOptions,
  saveOrUpdateHandle,
  checkMenuFormComputed,
} from './data/useMenu'
import {
  NSpace,
  NButton,
  NIcon,
  NDataTable,
  NModal,
  NText,
  NInputGroup,
  NInputGroupLabel,
  NInput,
  NSelect,
  NSwitch,
} from "naive-ui";
import DataTableSetting from "~/components/common/dataTable/DataTableSetting.vue"
import DescSetting from "~/components/DescSetting.vue"
import {onMounted} from "vue";

onMounted(() => {
  createMenuData()
})

</script>

<template>
  <n-space vertical>
    <n-space justify="end">
      <n-button secondary strong size="small" @click="showSaveOrUpdateModelHandle">
        <template #icon>
          <n-icon>
            <Add/>
          </n-icon>
        </template>
        新增菜单
      </n-button>
      <data-table-setting/>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="menuData"
        :striped="tableSet.striped"
        :single-line="tableSet.singleLine"
        :size="tableSize"
        size="tableSize"
        :scroll-x="1200"
        remote
        :loading="loading"
        :row-key="(row: MenuBackModel) => row.id"
    />
    <n-space vertical>
      <n-modal v-model:show="showSaveOrUpdateModel" preset="dialog" title="Dialog" :show-icon="false"
               :mask-closable='false'>
        <template #header>
          <n-text v-if="!menuForm.id">新增菜单</n-text>
          <n-text v-else>修改菜单</n-text>
        </template>
        <n-space vertical>
          <n-input-group>
            <n-input-group-label>标题</n-input-group-label>
            <n-input v-model:value="menuForm.title" placeholder="菜单标题"
                     :status="menuForm.title.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>父菜单</n-input-group-label>
            <n-select v-model:value="menuForm.parentId" clearable :default-value="menuForm.parentId" size="medium"
                      :options="menuParentOptions" placeholder="父菜单，为空则当前为父菜单"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>路由</n-input-group-label>
            <n-input v-model:value="menuForm.path" placeholder="访问路由。vue-router规范"
                     :status="menuForm.path.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>标识</n-input-group-label>
            <n-input v-model:value="menuForm.name" placeholder="组件名称。例：HomeMenu"
                     :status="menuForm.name.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>组件</n-input-group-label>
            <n-input v-model:value="menuForm.component" placeholder="组件路径。例：/home/Home.vue"
                     :status="menuForm.component.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group>
            <n-input-group-label>图标</n-input-group-label>
            <n-input v-model:value="menuForm.icon" placeholder="菜单图标（IconPark）"
                     :status="menuForm.icon.length === 0 ? 'error':'success'"/>
          </n-input-group>
          <n-input-group v-if="menuForm.id !== null">
            <n-input-group-label>排序</n-input-group-label>
            <n-input v-model:value="menuForm.orderNum" placeholder="菜单展示的顺序"
                     :status="menuForm.orderNum === null ? 'error':'success'"/>
          </n-input-group>
          <desc-setting title="状态">
            <n-switch :round="false" v-model:value="menuForm.isEnable">
              <template #checked>
                开启
              </template>
              <template #unchecked>
                禁用
              </template>
            </n-switch>
          </desc-setting>
        </n-space>
        <template #action>
          <n-button :disabled="!checkMenuFormComputed" tertiary size="small" type="warning" @click="saveOrUpdateHandle">
            保存
          </n-button>
        </template>
      </n-modal>
    </n-space>
  </n-space>
</template>

<style scoped>

</style>