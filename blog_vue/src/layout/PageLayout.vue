<script setup lang='ts'>
import live2d from 'vue-live2d'
</script>

<template>
  <div class="layout-wrapper">
    <header class="header-wrapper">
      <slot name="header"/>
    </header>
    <main class="main-wrapper">
      <slot name="content"/>
    </main>
    <footer class="footer-wrapper">
      <slot name="footer"/>
    </footer>
    <div class="fixed right-0 -bottom-10px <sm:(hidden)">
      <live2d
          direction="right"
          :size="180"
          homePage="https://github.com/apecodex"
      />
    </div>
  </div>
</template>

<style scoped>
.layout-wrapper {
  @apply w-full h-full flex flex-col;
}

.header-wrapper {
  height: 100vh;
}

.main-wrapper {
  @apply 'w-full max-w-1200px m-auto mt-15px px-10px';
}
</style>
<style>
.vue-live2d-tool {
  @apply 'right-10px';
}
.vue-live2d-toggle {
  @apply '!bg-$theme-bg-reverse !text-$text-color-reverse mb-12px';
}
.vue-live2d-tip {
  @apply '!p-10px';
}
.vue-live2d-tip-on-top {
  @apply '!bottom-[80%] !top-auto';
}
</style>