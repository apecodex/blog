<script setup lang='ts'>
import useTagsView from './useTagsView';
import useDropDown from './useDropdown';
import {Close} from '@icon-park/vue-next'
import {useRoute} from "vue-router";
import {toRaw} from "vue";
import {
  NScrollbar,
  NSpace,
  NTag,
  NDropdown
} from "naive-ui"

const route = useRoute()
const {handleClickTag, handleClose, visitedList} = useTagsView();

const {
  showDropRef,
  x,
  y,
  handleSelect,
  handleContextMenu,
  clickoutSide,
  dropOptions
} = useDropDown(toRaw(visitedList.value))

</script>

<template>
  <n-scrollbar x-scrollable>
    <n-space size="small" align="center" class="h-33px p-1" style="flex-flow: inherit">
      <n-tag v-for="i in visitedList" :key="i.fullPath" :bordered="false" @close="() => handleClose(i)"
             :color="i.fullPath === route.fullPath ? { color: 'var(--n-color-checked)', textColor: 'var(--n-text-color-checked)' } : {}"
             class="cursor-pointer">
      <span class="hover:(text-orange-200) transition-all duration-300" @click="() => handleClickTag(i)"
            @contextmenu="(...args) => handleContextMenu(...args, i)">{{
          i.title
        }}
      </span>
        <Close class="ml-5px" v-if="i.fullPath !== '/'" @click="handleClose(i)" theme="outline" fill="#636363"/>
      </n-tag>
      <n-dropdown placement='bottom-start' show-arrow trigger="manual" :x="x" :y="y" :options="(dropOptions as any)"
                  :show="showDropRef" @clickoutside="clickoutSide" @select="handleSelect"/>
    </n-space>
  </n-scrollbar>
</template>

<style scoped>
</style>