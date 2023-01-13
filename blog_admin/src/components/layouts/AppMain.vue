<script setup lang='ts'>
import type { PropType } from 'vue';
import { useGlobalStore } from '~/store';
import {storeToRefs} from "pinia";
import {NSpin} from "naive-ui"

const globalStore = useGlobalStore();
let { isRouterAlive } = storeToRefs(globalStore)

const props = defineProps({
  cachedViews: Array as PropType<string[]>
})
</script>

<template>
  <suspense>
    <template #default>
      <router-view v-slot="{ Component }" v-if="isRouterAlive">
        <Transition name="fade-transform" mode="out-in">
          <keep-alive :include="props.cachedViews">
            <component :is="Component" />
          </keep-alive>
        </Transition>
      </router-view>
    </template>
    <template #fallback>
      <n-spin size="large"></n-spin>
    </template>
  </suspense>
</template>

<style scoped>
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s ease 0s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>