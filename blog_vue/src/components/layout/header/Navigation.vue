<script setup lang='ts'>
import {computed, ref, onMounted, Ref} from "vue";
import {ToTop, Search, SettingTwo, Theme} from '@icon-park/vue-next'
import {useSettingStore, useWebsiteInfoStore} from "@/store";
import {storeToRefs} from "pinia";
import {scrollUp} from '@/utils/utils'
import {
  navAreaRef,
  websiteRef,
  navAreaClassActive,
  websiteClassActive,
  effect1configClassActive,
  navScrollHandle,
} from './headerHooks'
import NavPc from './NavPc.vue'
import NavSmall from './NavSmall.vue'

const settingStore = useSettingStore()
const {globalTheme, searchFlag, articleThemeFlag} = storeToRefs(settingStore)

const themeComp = computed({
  get: () => globalTheme.value === "darkTheme" ? 'dark' : '',
  set: (v: any) => {
    globalTheme.value = v
    document.documentElement.className = themeComp.value
  }
}) as any

const websiteInfoStore = useWebsiteInfoStore()
const {websiteInfo} = storeToRefs(websiteInfoStore)

const themeValue = ref(globalTheme.value !== "darkTheme")

// 初始化主题
document.documentElement.className = themeComp.value

const changeTheme = () => {
  themeValue.value ? themeComp.value = 'darkTheme' : themeComp.value = 'lightTheme'
}

onMounted(() => {
  navScrollHandle()
})
</script>

<template>
  <header ref="navAreaRef" class="nav-area fixed top-0 w-full h-44px z-1" :class="navAreaClassActive">
    <div class="flex justify-between items-center h-full py-0 px-10px">
      <div class="nav-logo relative h-full z-1" @click="scrollUp">
        <router-link to="/" class="text-26px leading-44px">
          <h1 class="logo-text" :data-text="websiteInfo?.websiteName">{{ websiteInfo?.websiteName }}</h1>
        </router-link>
      </div>
      <div class="flex justify-center items-center gap-5px">
        <div
            class="nav-search flex justify-center items-center gap-5px p-5px hover:(bg-$theme-bg-reverse text-$text-color-reverse rounded-4px)"
            @click="searchFlag = true">
          <Search size="20"/>
          搜索
        </div>
        <nav-pc/>
        <nav-small/>
      </div>
    </div>
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160"
         class="nav-effect1config nav-effect1config-left" :class="effect1configClassActive">
      <path id="Trazado_200" data-name="Trazado 200"
            d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
            transform="translate(0 10)"></path>
    </svg>
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160"
         class="nav-effect1config nav-effect1config-right" :class="effect1configClassActive">
      <path id="Trazado_200" data-name="Trazado 200"
            d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
            transform="translate(0 10)"></path>
    </svg>
  </header>
  <div ref="websiteRef"
       class="website-container z-5 fixed left-0 bottom-0 w-90px h-30px bg-$theme-bg2 flex justify-center items-center gap-12px text-$theme-bg-reverse"
       :class="websiteClassActive">
    <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160"
         class="effect1config">
      <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
            transform="translate(0 10)"></path>
    </svg>
    <div class="setting-icon cursor-pointer avatar">
      <SettingTwo size="24"/>
    </div>
    <label class="text-12px relative inline-block w-41px h-22px rounded-30px">
      <input class="theme-checkbox opacity-0 w-0 h-0" type="checkbox" v-model="themeValue" @click="changeTheme">
      <span
          class="theme-slider absolute cursor-pointer top-0 left-0 right-0 bottom-0 bg-$theme-bg rounded-30px duration-500 cursor-pointer"></span>
    </label>
    <!-- 侧边设置 -->
    <div class="website-side-wrapper active fixed bottom-45px -left-full" :class="websiteClassActive" style="border-radius: 0 17px 17px 0;">
      <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160"
           class="effect1config normal">
        <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
              transform="translate(0 10)"></path>
      </svg>
      <ul class="bg-$theme-bg2 py-8px px-5px flex flex-col gap-8px z-5" style="border-radius: 0 17px 17px 0;transition: var(--theme-transition-bg)">
        <li class="setting-item flex justify-center items-center relative" @click="scrollUp">
          <div class="p-3px rounded-full text-$text-color">
            <ToTop size="22"/>
          </div>
          <span class="setting-tips -z-1 absolute -left-2/1 whitespace-nowrap bg-$text-color text-$text-color-reverse py-2px px-5px rounded-6px w-auto w-max">滚动到顶部</span>
        </li>
        <li class="setting-item flex justify-center items-center relative" @click="articleThemeFlag = true">
          <div class="p-3px rounded-full text-$text-color">
            <Theme size="22"/>
          </div>
          <span class="setting-tips -z-1 absolute -left-2/1 whitespace-nowrap bg-$text-color text-$text-color-reverse py-2px px-5px rounded-6px w-auto w-max">修改文章预览主题</span>
        </li>
      </ul>
      <svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160"
           class="effect1config invert">
        <path id="Trazado_200" data-name="Trazado 200" d="M0-10,150,0l10,150S137.643,80.734,100.143,43.234,0-10,0-10Z"
              transform="translate(0 10)"></path>
      </svg>
    </div>
  </div>
</template>

<style scoped>
.nav-area {
  transition: top .6s, var(--theme-transition-bg);
}

.logo-text::after {
  content: attr(data-text);
  color: transparent;
  position: absolute;
  top: 0;
  left: 0;
  animation: spotlight 5s infinite;
  background: #833ab4;
  background: -webkit-linear-gradient(to right, #fcb045, #fd1d1d, #833ab4);
  background: linear-gradient(to right, #fcb045, #fd1d1d, #833ab4);
  -webkit-background-clip: text;
  background-clip: text;
}

.nav-search {
  transition: var(--theme-transition-bg);
  border-radius: 7px 20px 20px 20px;
}

.nav-search:hover {
  border-radius: 7px 20px 20px 20px;
}

/*滚动条往下时*/
.nav-area.nav-fixed {
  @apply '-top-71px';
}

/*滚动条往上时*/
.nav-area.nav-sticky {
  @apply 'top-0 z-8 bg-$theme-bg';
}

/*滚动条到顶时*/
.nav-area.nav-top {
  @apply 'top-0 bg-transparent';
}

/* 导航下面贴着的两个小角 */

.nav-effect1config,
.effect1config {
  transition: var(--theme-transition-fill)
}

.nav-effect1config {
  @apply 'w-60px h-60px top-38px absolute fill-transparent';
}

.nav-effect1config-left {
  @apply '-left-7px';
  transform: rotate(280deg);
}

.nav-effect1config-right {
  @apply '-right-7px';
  transform: rotate(-10deg);
}

.nav-effect1config-left.effect-fixed {
  @apply '-left-full';

}
.nav-effect1config-right.effect-fixed {
  @apply '-right-full';
}

/* 向下滚动时隐藏 */
.nav-effect1config-left.effect-fixed, .nav-effect1config-right.effect-fixed {
  @apply 'opacity-0';
  transition: all 1s ease, opacity .5s ease-out;
}

/* 向上滚动时展示 */
.nav-effect1config-left.effect-sticky, .nav-effect1config-right.effect-sticky {
  @apply 'fill-$theme-bg';
}

/* 到达顶部时隐藏*/
.nav-effect1config-left.effect-top {
  @apply '-left-full';
  transition: all 1s !important;
}

/* 到达顶部时隐藏*/
.nav-effect1config-right.effect-top {
  @apply '-right-full';
  transition: all 1s !important;
}

/* 网站设置 */
.website-container {
  @apply '-left-100px';
  border-radius: 0 16px 6px 0;
  transition: left .6s ease, var(--theme-transition);
}

.effect1config {
  @apply 'w-50px h-50px -top-48px -left-5px absolute fill-$theme-bg2';
  transform: rotate(176deg);
  transition: var(--theme-transition-fill)
}

.effect1config.normal {
  @apply 'relative -left-12px top-3px';
  transform: rotate(176deg);
}

.effect1config.invert {
  @apply 'relative -left-12px -top-3px';
  transform: rotate(-86deg);
  bottom: 0;
}

.website-side-wrapper {
  transition: left .3s ease-in-out, var(--theme-transition);
}

.website-side-wrapper::after {
  @apply 'w-full h-30px -bottom-20px absolute';
  content: "";
}

.setting-icon:hover ~ .website-side-wrapper, .website-side-wrapper:hover {
  @apply 'left-0';
}

.website-side-wrapper ul li.setting-item div {
  transition: var(--theme-transition-shadow), var(--theme-transition);
}
.website-side-wrapper ul li.setting-item div:hover {
  @apply 'bg-$text-color text-$text-color-reverse';
}

/* 提示 */
.setting-item .setting-tips {
  transition: left .1s ease-in-out;
}

.setting-item .setting-tips::before {
  @apply 'absolute -left-19px top-1/2';
  content: "";
  border-left: 10px solid transparent;
  border-right: 10px solid var(--text-color);
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  transform: translateY(-50%);
}
.setting-item:hover .setting-tips {
  @apply 'left-60px';
  animation: justshake 0.5s forwards;
}

.theme-slider:before {
  @apply 'absolute h-1.4em w.1.4em rounded-1/2 bg-$heme-bg';
  position: absolute;
  content: "";
  height: 1.4em;
  width: 1.4em;
  border-radius: 50%;
  left: 10%;
  bottom: 15%;
  box-shadow: inset 8px -4px 0 0 #fff000;
  transition: .5s;
}

.theme-checkbox:checked + .theme-slider {
  @apply 'bg-$theme-bg';
}

.theme-checkbox:checked + .theme-slider:before {
  transform: translateX(100%);
  box-shadow: inset 15px -4px 0 15px #fff000;
}


.website-container.website-fixed{
  @apply 'left-0';
}
/* 到达顶部时隐藏 */
.website-container.website-sticky {
  @apply '-left-full';
}

@keyframes spotlight {
  0% {
    -webkit-clip-path: ellipse(10px 100px at 0% 50%);
    clip-path: ellipse(10px 100px at 0% 50%);
  }
  50% {
    -webkit-clip-path: ellipse(10px 100px at 100% 50%);
    clip-path: ellipse(10px 1000px at 100% 50%);
  }
  100% {
    -webkit-clip-path: ellipse(10px 100px at 0% 50%);
    clip-path: ellipse(10px 100px at 0% 50%);
  }
}
</style>