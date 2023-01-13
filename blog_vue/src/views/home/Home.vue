<script setup lang='ts'>
import LRLayout from '@/layout/LRLayout.vue'
import ArticleList from './ArticleList.vue'
import BlogInfo from './BlogInfo.vue'
</script>

<template>
  <LRLayout>
    <template #content>
      <ArticleList/>
    </template>
    <template #info>
      <BlogInfo/>
    </template>
  </LRLayout>
</template>

<style scoped>
</style>