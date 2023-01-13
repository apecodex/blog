<script setup lang='ts'>
import iconMap from '~/utils/icons'
import {computed, h} from 'vue'
import {useRoute} from "vue-router";
import {NBreadcrumb, NBreadcrumbItem, NIcon} from "naive-ui"

const props = defineProps({
  isShowBreadcrumbIcon: Boolean
})
const route = useRoute()
const breadList = computed(() => {
  return route.matched.slice(1, route.matched.length).map((i) => {
    return {
      title: i.meta.title,
      icon: i.meta.icon,
      href: i.path,
      clickable: !!i.redirect && !!i.children.length
    }
  }).filter((i) => i.href !== '/')
})

</script>

<template>
  <n-breadcrumb separator="/">
    <n-breadcrumb-item v-for="breadItem in  breadList" :key="breadItem.href" :clickable="breadItem.clickable"
                       :href="breadItem.clickable ? breadItem.href : undefined">
      <n-icon v-if="props.isShowBreadcrumbIcon" size="20"
              :component="typeof breadItem.icon === 'string' ? h(iconMap.get(breadItem.icon)) : breadItem.icon"/>
      {{ breadItem.title }}
    </n-breadcrumb-item>
  </n-breadcrumb>
</template>

<style scoped>
</style>