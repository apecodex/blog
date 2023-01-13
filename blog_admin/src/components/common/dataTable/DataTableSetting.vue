<template>
  <n-space>
    <n-select size="small" :style="{width: '70px'}" placeholder="尺寸" :options="tableSizeOption"
              :consistent-menu-width="false" @update:value="tableSizeHandle" :default-value="tableSize"/>
    <n-popover trigger="click" style="width: 200px" placement="left">
      <template #trigger>
        <n-button secondary size="small">
          <template #icon>
            <n-icon>
              <Config theme="outline"/>
            </n-icon>
          </template>
          设置
        </n-button>
      </template>
      <desc-setting title="斑马线条纹">
        <n-switch :round="false" v-model:value="tableSet.striped"
                  @update:value="(value) => tableSet.striped = value"/>
      </desc-setting>
      <n-divider/>
      <desc-setting title="列的分割线">
        <n-switch :round="false" v-model:value="tableSet.singleLine"
                  @update:value="(value) => tableSet.singleLine = value"/>
      </desc-setting>
      <n-divider/>
      <desc-setting title="行的分割线">
        <n-switch :round="false" v-model:value="tableSet.singleColumn"
                  @update:value="(value) => tableSet.singleColumn = value"/>
      </desc-setting>
    </n-popover>
  </n-space>
</template>

<script setup lang="ts">
import {tableSizeOption, tableSize, tableSet, tableSizeHandle} from "./index";
import {Config} from '@icon-park/vue-next';
import DescSetting from "~/components/DescSetting.vue"
import {
  NSelect,
  NPopover,
  NSpace,
  NIcon,
  NDivider,
  NSwitch,
  NButton
} from "naive-ui";
</script>

<style scoped>
/* 修改分割线margin */
.n-divider:not(.n-divider--vertical) {
  margin-top: 5px !important;
  margin-bottom: 5px !important;
}
</style>