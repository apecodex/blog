<script setup lang='ts'>
const props = defineProps({
  title: String,
  descClass: {
    type: String,
    default: ''
  }
})
</script>

<template>
  <div class="flex justify-between items-center text-16px">
    <span :class="descClass">{{ props.title }}</span>
    <slot />
  </div>
</template>

<style scoped>
</style>