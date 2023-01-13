<script setup lang='ts'>
import {NEl} from "naive-ui"
const props = defineProps({
  isCollapse: Boolean
})
</script>

<template>
  <n-el class="logo">
    <div class="w-full h-full flex justify-center items-center">
      <img class="w-36px h-36px" src="/sleaves.svg" alt="">
      <span v-if="!props.isCollapse" class="logo-span">Blog
        Admin</span>
    </div>
  </n-el>
</template>

<style scoped>
.logo {
  border-bottom: solid 1px var(--n-border-color);
  background-color: var(--n-color);
  transition: color 0.3s var(--n-bezier), border-color 0.3s var(--n-bezier),
    min-width 0.3s var(--n-bezier), max-width 0.3s var(--n-bezier),
    transform 0.3s var(--n-bezier), background-color 0.3s var(--n-bezier);
  @apply w-200px h-50px max-w-200px z-2
}

.logo-span {
  transition: margin-left 0.3s var(--n-bezier), color 0.3s var(--n-bezier),
    opacity 0.3s var(--n-bezier);
  @apply font-700 opacity-100 whitespace-nowrap ml-15px text-18px
}
</style>