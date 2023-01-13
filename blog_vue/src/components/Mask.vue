<script setup lang='ts'>
const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
})

</script>

<template>
  <div class="fixed top-0 left-0 right-0 bottom-0 w-full h-full z-9 bg-$theme-translucent" style="transition: var(--theme-transition-bg);"  v-show="show">
    <slot/>
  </div>
</template>

<style scoped>

</style>