<script setup lang="ts">
import type {Ref} from "vue";
import BoxComponent from "@/components/BoxComponent.vue";
import {
  LeftSmall,
  RightSmall,
  Redo,
  UpSmall,
  CategoryManagement,
  TagOne,
  Copy,
  Platte,
  CopyOne,
  Clipboard,
  Search,
  CopyLink
} from "@icon-park/vue-next"
import Mask from "@/components/Mask.vue";
import {onMounted, ref} from "vue";
import {
  rightMenu,
  copyText,
  searchBaidu,
  copyPageUrl,
  changeTheme,
  closeRightMenu,
  pauseText
} from "@/components/rightMenu/rightMenuHooks"
import {useRouter} from "vue-router"
import {scrollUp} from "@/utils/utils";
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";

const router = useRouter();
const settingStore = useSettingStore()
const {theme} = storeToRefs(settingStore);
const menuRef: Ref<HTMLElement | null> = ref(null);

onMounted(() => {
  rightMenu.value.menuWidth = menuRef.value?.offsetWidth as number;
  rightMenu.value.menuHeight = menuRef.value?.offsetHeight as number;
})

</script>

<template>
  <Mask class="bg-transparent" :show="rightMenu.flag" @click="closeRightMenu"/>
  <box-component>
    <div ref="menuRef"
         class="menu z-9999 p-8px absolute min-w-160px border-1px border-$theme-bg border-dashed rounded-10px bg-$theme-bg-reverse text-$text-color-reverse"
         :class="{'is-active': rightMenu.flag}"
         style="width: fit-content;" :style="{'top': rightMenu.top + 'px', 'left': rightMenu.left + 'px'}">
      <div class="flex items-center justify-between">
        <span class="p-5px shadow-hover-reverse rounded-6px cursor-pointer" @click="router.go(-1)"><LeftSmall size="16"
                                                                                                              :strokeWidth="6"/></span>
        <span class="p-5px shadow-hover-reverse rounded-6px cursor-pointer" @click="router.go(1)"><RightSmall size="16"
                                                                                                              :strokeWidth="6"/></span>
        <span class="p-5px shadow-hover-reverse rounded-6px cursor-pointer" @click="router.go()"><Redo size="16"
                                                                                                       :strokeWidth="6"/></span>
        <span class="p-5px shadow-hover-reverse rounded-6px cursor-pointer" @click="scrollUp()"><UpSmall size="16"
                                                                                                         :strokeWidth="6"/></span>
      </div>
      <hr class="border-t-1px my-10px border-dashed border-$theme-translucent">
      <div v-if="!rightMenu.pluginMode">
        <div class="menu-item mb-5px" @click="router.push({name: 'CategoryNav'})">
          <CategoryManagement/>
          <span class="ml-6px">文章分类</span>
        </div>
        <div class="menu-item" @click="router.push({name: 'TagNav'})">
          <TagOne/>
          <span class="ml-6px">文章标签</span>
        </div>
      </div>
      <div v-else>
        <div class="menu-item mb-5px" @click="copyText(rightMenu.copyText)" v-if="rightMenu.copyText.length !== 0">
          <CopyOne/>
          <span class="ml-6px">复制选中文本</span>
        </div>
        <div class="menu-item mb-5px" @click="copyText(rightMenu.href)" v-if="rightMenu.href.length !== 0">
          <CopyLink/>
          <span class="ml-6px">复制链接</span>
        </div>
        <div class="menu-item mb-5px" @click="copyText(rightMenu.imgSrc)" v-if="rightMenu.imgSrc.length !== 0">
          <CopyLink/>
          <span class="ml-6px">复制图片链接</span>
        </div>
        <div class="menu-item" @click="searchBaidu">
          <Search/>
          <span class="ml-6px">百度搜索</span>
        </div>
      </div>
      <div class="menu-item mt-5px" @click="pauseText">
        <Clipboard/>
        <span class="ml-6px flex w-full justify-between">粘贴<span>ctrl + v</span></span>
      </div>
      <hr class="border-t-1px my-10px border-dashed border-$theme-translucent">
      <div>
        <div class="menu-item mb-5px" @click="copyPageUrl">
          <Copy/>
          <span class="ml-6px">分享页面</span>
        </div>
        <div class="menu-item" @click="changeTheme">
          <Platte/>
          <span class="ml-6px">{{ theme ? "浅色模式" : "暗色模式" }}</span>
        </div>
      </div>
    </div>
  </box-component>
</template>

<style scoped>
.menu {
  @apply 'fixed opacity-0 invisible min-w-130px';
  transition: opacity .3s ease, min-width .3s ease, visibility .3s ease;
}

.is-active {
  @apply 'opacity-100 visible min-w-160px';
}

.menu-item {
  @apply 'flex justify-start items-center py-2px pl-8px rounded-8px';
}

.menu-item:hover {
  box-shadow: var(--theme-shadow-inset-reverse), var(--theme-shadow-reverse);
}
</style>