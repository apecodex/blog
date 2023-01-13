<script setup lang='ts'>
import type {SelectOption} from 'naive-ui';
import {
  NDrawer,
  NDrawerContent,
  NDivider,
  NSpace,
  NSwitch,
  NSelect,
  NInputNumber
} from 'naive-ui';

import DescSetting from "~/components/DescSetting.vue"
import {useSettingStore} from '~/store'
import {storeToRefs} from "pinia";
import {ref} from "vue"

const settingStore = useSettingStore()
const {
  isShowDraw,
  isShowLogo,
  isFixedHeader,
  isShowTagViews,
  isShowBreadcrumb,
  isShowBreadcrumbIcon,
  isInverted,
  triggerStyle,
  collapsedWidth,
  collapsedIconSize,
  globalTheme,
  menuMode
} = storeToRefs(settingStore)

// 主题色
const themeOptions = ref<SelectOption[]>([
  {label: "亮色主题", value: "lightTheme"},
  {label: "暗色主题", value: "darkTheme"}
])

// 折叠菜单风格
const triggerOptions = ref<SelectOption[]>([
  {label: '竖线', value: 'bar'},
  {label: '圆角', value: 'arrow-circle'},
  {label: '自定义', value: 'custom'}
])

// 菜单风格
const menuStyleOptions = ref<SelectOption[]>([
  {label: '侧栏菜单', value: 'vertical'},
  {label: '顶栏菜单', value: 'horizontal'}
])
</script>

<template>
  <n-drawer v-model:show="isShowDraw" width="300">
    <n-drawer-content title="界面显示设置">
      <n-divider>主题</n-divider>
      <n-space vertical>
        <desc-setting title="主题色" desc-class="w-full">
          <n-select class="w-1/2" v-model:value="globalTheme" :options="themeOptions" :consistent-menu-width="false"/>
        </desc-setting>
      </n-space>
      <n-divider>配置项</n-divider>
      <n-space vertical>
        <desc-setting title="固定头部" desc-class="w-full">
          <n-switch v-model:value="isFixedHeader"/>
        </desc-setting>
        <desc-setting title="显示多标签" desc-class="w-full">
          <n-switch v-model:value="isShowTagViews"/>
        </desc-setting>
        <desc-setting title="显示面包屑" desc-class="w-full">
          <n-switch v-model:value="isShowBreadcrumb"/>
        </desc-setting>
        <desc-setting title="显示面包屑图标" v-show="isShowBreadcrumb" desc-class="w-full">
          <n-switch v-model:value="isShowBreadcrumbIcon"/>
        </desc-setting>
        <desc-setting title="折叠图标风格" desc-class="w-full">
          <n-select class="w-1/2" v-model:value="triggerStyle" :options="triggerOptions" :consistent-menu-width="false">
          </n-select>
        </desc-setting>
        <desc-setting title="显示Logo" desc-class="w-full">
          <n-switch v-model:value="isShowLogo"/>
        </desc-setting>
        <desc-setting title="反转主题色" desc-class="w-full">
          <n-switch v-model:value="isInverted"/>
        </desc-setting>
        <desc-setting title="菜单风格" desc-class="w-full">
          <n-select class="w-1/2" v-model:value="menuMode" :options="menuStyleOptions" :consistent-menu-width="false"/>
        </desc-setting>
        <desc-setting title="菜单图标大小" desc-class="w-full">
          <n-input-number class="w-1/2" v-model:value="collapsedIconSize" :max="50" :min="10"/>
        </desc-setting>
        <desc-setting title="菜单折叠宽度" desc-class="w-full">
          <n-input-number class="w-1/2" v-model:value="collapsedWidth" :max="64" :min="48"/>
        </desc-setting>
      </n-space>
    </n-drawer-content>
  </n-drawer>
</template>

<style scoped>
</style>