<script setup lang='ts'>
import {useSettingStore, useTagsViewStore, useWebsiteStore} from '~/store'
import {computed, onMounted} from "vue";
import {listNoticeBack} from "~/api/requests/Notice";
import {storeToRefs} from "pinia";
import {
  NLayout,
  NLayoutContent,
  NEl,
  NBackTop,
} from "naive-ui"
import BaseSider from "~/components/layouts/BaseSider.vue"
import BaseHeader from "~/components/layouts/BaseHeader.vue"
import AppMain from "~/components/layouts/AppMain.vue"
import TagsView from "~/components/layouts/tagsView/TagsView.vue"
import GlobalDraw from "~/components/GlobalDraw.vue"


const settingStore = useSettingStore();
const tagsViewStore = useTagsViewStore();
const websiteStore = useWebsiteStore();
const {cachedViews} = storeToRefs(tagsViewStore);
const {noReadNoticeCount} = storeToRefs(websiteStore);

const {
  isShowLogo,
  isCollapse,
  isFixedHeader,
  isShowTagViews,
  isInverted,
  triggerStyle,
  collapsedIconSize,
  collapsedWidth,
  menuMode
} = storeToRefs(settingStore);

const contentStyle = computed(() => {
  if (isFixedHeader.value) {
    if (isShowTagViews.value) {
      return {marginTop: '84px'}
    } else {
      return {marginTop: '50px'}
    }
  } else {
    return {}
  }
})

onMounted(async () => {
  // 获取消息通知数量
  const noticeBackResponse = await listNoticeBack({isRead: false, isSystemNotice: true});
  if (noticeBackResponse.status) {
    noReadNoticeCount.value = noticeBackResponse.data.count;
  }
})
</script>

<template>
  <n-layout has-sider position="absolute">
    <base-sider v-if="menuMode === 'vertical'" :is-show-logo="isShowLogo" :trigger-style="triggerStyle"
                :is-collapse="isCollapse" :collapsed-icon-size="collapsedIconSize" :collapsed-width="collapsedWidth"
                :menu-mode="menuMode" @change-collapsed="settingStore.changeSetting" :is-inverted="isInverted"/>
    <n-layout :native-scrollbar="false">
      <base-header :is-collapse="isCollapse" :trigger-style="triggerStyle" :is-inverted="isInverted"
                   :collapsed-icon-size="collapsedIconSize" :collapsed-width="collapsedWidth" :menu-mode="menuMode"/>
      <n-el v-if="isShowTagViews" class="tag-views h-34px">
        <tags-view/>
      </n-el>
      <n-layout-content :position="isFixedHeader ? 'absolute' : 'static'" :native-scrollbar="false"
                        :content-style="{ padding: '10px' }" :style="contentStyle">
        <app-main :cached-views="cachedViews"/>
        <n-back-top style="z-index: 10" right="15"/>
      </n-layout-content>
    </n-layout>
    <global-draw/>
  </n-layout>

</template>

<style scoped>
.tag-views {
  background: var(--body-color);
  border-bottom: 1px solid var(--divider-color);
  transition: all 0.3s var(--cubic-bezier-ease-in-out);
}
</style>