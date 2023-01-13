<script setup lang='ts'>
import type {PropType} from 'vue'
import type {MenuMode, TriggerStyle} from '~/settings'
import {computed} from "vue";
import {NLayoutSider} from "naive-ui"
import BaseLogo from "~/components/layouts/BaseLogo.vue"
import Menu from "~/components/layouts/Menu.vue"

type Trigger = boolean | 'bar' | 'arrow-circle'

const props = defineProps({
  isShowLogo: Boolean,
  triggerStyle: String as PropType<TriggerStyle>,
  isCollapse: Boolean,
  isInverted: Boolean,
  collapsedWidth: Number,
  collapsedIconSize: Number,
  menuMode: {
    type: String as PropType<MenuMode>,
    default: 'vertical'
  }
})
const emits = defineEmits(['changeCollapsed'])
const triggerStyle = computed(() => props.triggerStyle === 'custom' ? false : props.triggerStyle)

</script>

<template>
  <n-layout-sider :inverted="props.isInverted" :width="200" bordered :native-scrollbar="false"
                  :show-trigger="triggerStyle" collapse-mode="width" :collapsed-width="props.collapsedWidth"
                  :collapsed="props.isCollapse"
                  v-on:update:collapsed="emits('changeCollapsed', 'isCollapse', !props.isCollapse)">
    <base-logo v-if="props.isShowLogo" :is-collapse="props.isCollapse"
               :class="props.menuMode === 'vertical' ? 'logoFixedBorder' : ''"
               :style="props.isCollapse ? { maxWidth: `${props.collapsedWidth}px` } : {}"/>
    <Suspense>
      <Menu :is-inverted="props.isInverted" :collapsed-icon-size="props.collapsedIconSize"
            :collapsed-width="props.collapsedWidth" :menu-mode="props.menuMode"
            :style="props.isShowLogo ? { marginTop: '50px' } : {}"/>
    </Suspense>
  </n-layout-sider>
</template>

<style scoped>
.logoFixedBorder {
  @apply fixed top-0 left-0 border-solid border border-$n-border-color
}
</style>