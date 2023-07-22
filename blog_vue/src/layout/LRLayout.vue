<script setup lang='ts'>

</script>

<template>
  <div class="relative w-full h-full flex justify-between gap-20px">
    <div class="left-wrapper w-full flex flex-col gap-20px">
      <slot name="content" />
    </div>
    <div class="right-wrapper w-full flex flex-col gap-20px">
      <slot name="info" />
    </div>
  </div>
</template>

<style scoped>
.left-wrapper {
  flex: 0 0 75%;
}

.right-wrapper {
  @apply 'sticky top-15px left-0 w-full h-full';
}

@media (max-width: 960px) {
  .left-wrapper {
    @apply 'w-full flex-1';
  }

  .right-wrapper {
    @apply 'hidden';
  }
}
</style>