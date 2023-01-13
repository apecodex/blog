<script setup lang='ts'>
import {useGlobalStore} from "@/store"
import {storeToRefs} from "pinia";

const globalStore = useGlobalStore();
const {isRouterAlive} = storeToRefs(globalStore);
</script>

<template>
  <suspense>
    <router-view v-slot="{Component}" v-if="isRouterAlive">
      <transition name="fade-transform">
        <component :is="Component"/>
      </transition>
    </router-view>
  </suspense>
</template>

<style scoped>
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: transform 0.5s ease 0s, opacity 0.5s ease;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateY(-100px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateY(100px);
}
</style>