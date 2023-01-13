<script setup lang='ts'>
import BoxComponent from "@/components/BoxComponent.vue";
const props = defineProps({
  customizeClass: String
})
</script>

<template>
  <BoxComponent class="loading w-100px h-50px" :class="[customizeClass]"/>
</template>

<style scoped>
.loading {
  background: linear-gradient(
      100deg, rgba(255,255,255,0) 40%,
    rgba(255,255,255,.5) 50%,
    rgba(255,255,255, 0) 60%
  ) var(--theme-bg);
  background-size: 200% 100%;
  background-position-x: 180%;
  animation: 1s loading ease-in-out infinite;
  transition: var(--theme-transition-bg), var(--theme-transition-shadow);
}

@keyframes loading {
  to {
    background-position-x: -20%;
  }
}
</style>