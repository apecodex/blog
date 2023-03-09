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