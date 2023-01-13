<script setup lang='ts'>
import { MenuUnfoldOne, MenuFoldOne } from '@icon-park/vue-next';
import {NEl} from "naive-ui";

const props = defineProps({
  isCollapse: Boolean
})
const emits = defineEmits(['changeSetting'])

</script>

<template>
  <n-el class="cursor-pointer w-50px h-50px flex justify-center items-center"
    @click="() => emits('changeSetting', 'isCollapse', !props.isCollapse)">
    <template v-if="isCollapse">
      <menu-fold-one theme="multi-color" size="28" :fill="['#333', '#2F88FF', '#FFF', '#43CCF8']" />
    </template>
    <template v-else>
      <menu-unfold-one theme="multi-color" size="28" :fill="['#333', '#2F88FF', '#FFF', '#43CCF8']" />
    </template>
  </n-el>
</template>

<style scoped>
</style>