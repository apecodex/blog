<script setup lang='ts'>
import {onBeforeMount, ref} from "vue";
import {getYiYan} from '@/api/requests/YiYan'
import {scrollDown} from '@/utils/utils'
import Navigation from './Navigation.vue'
import {useWebsiteInfoStore} from '@/store'
import {storeToRefs} from "pinia";
import {getBlogInfo} from "@/api/requests/WebsiteInfo";
import {notify} from "@kyvg/vue3-notification";

const websiteInfoStore = useWebsiteInfoStore();
const {websiteInfo} = storeToRefs(websiteInfoStore);

const IntroTextValue = ref("")
const yiYanTextValue = ref({
  hitokoto: "",
  from: ""
})

// 获取网站信息
const createWebsiteInfo = async () => {
  if (!websiteInfo.value) {
    await getBlogInfo().then((resp) => {
      if (resp.status) {
        websiteInfoStore.saveWebsiteInfo(resp.data)
      }
    }).catch(() => {
      notify({
        text: "网站信息获取失败，请重试！",
        type: 'warn'
      })
    }).finally(() => {
      if (websiteInfo.value) {
        introTextFunc()
      }
    })
  } else {
    introTextFunc()
  }
}

const introTextFunc = () => {
  const textArr = websiteInfo.value?.homeTyperTexts
  if (textArr) {
    let index = 0;
// 每行文本第几个字符
    let charIndex = 0;
// 间隔时间
    let delta = 500;
// 动画开始时间
    let start: number | null = null;
// 删除
    let isDeleting = false;
    const type = (time: number) => {
      window.requestAnimationFrame(type);
      if (!start) start = time;
      let progress = time - start;
      if (progress > delta) {
        let text = textArr[index];
        if (!isDeleting) {
          IntroTextValue.value = text.slice(0, ++charIndex);
          delta = 500 - Math.random() * 400
        } else {
          IntroTextValue.value = text.slice(0, charIndex--);
        }
        start = time;
        if (charIndex === text.length) {
          isDeleting = true;
          delta = 200;
          start = time + 1200;
        }
        if (charIndex < 0) {
          isDeleting = false;
          start = time + 200;
          index = ++index % textArr.length
        }
      }
    }
    window.requestAnimationFrame(type)
  }
}

const createYiYanText = async () => {
  await getYiYan().then((resp) => {
    if (resp) {
      yiYanTextValue.value = {
        hitokoto: resp.hitokoto,
        from: resp.from
      }
    }
  })
}

onBeforeMount(() => {
  createWebsiteInfo()
  createYiYanText()
})

</script>

<template>
  <section class="header-section">
    <div class="header-banner w-full h-full relative m-0 p-0 flex justify-center items-center flex-col">
      <h1 class="text-$text-color text-30px font-light z-1" style="transition: var(--theme-transition)">
        <span>{{ IntroTextValue }}</span>
        <span class="mark"></span>
      </h1>
      <div class="flex flex-col px-5px mt-15px z-1" v-show="yiYanTextValue.hitokoto.length !== 0">
        <span class="text-16px">{{ yiYanTextValue.hitokoto }}</span>
        <span class="text-right mt-10px <md:(text-12px)">-- {{ yiYanTextValue.from }}</span>
      </div>
      <ul class="slideshow">
        <li v-for="(bg, index) in websiteInfo?.websiteBackgroundImages" :key="index">
          <span :style="{backgroundImage: `url(${bg})`}"></span>
        </li>
      </ul>
      <div class="arrows-box absolute bottom-10px z-1 flex items-end justify-center cursor-pointer"
           style="transition: var(--theme-transition)" @click="scrollDown">
        <div class="scrolldown relative w-30px h-50px rounded-50px box-border mb-16px cursor-pointer cursor-pointer">
          <div class="chevrons pt-6px -ml-3px mt-48px w-30px flex flex-col items-center">
            <div class="chevrondown"></div>
            <div class="chevrondown"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="wave-wrapper">
      <svg class="wares" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
           viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
        <defs>
          <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"/>
        </defs>
        <g class="parallax">
          <use xlink:href="#gentle-wave" x="48" y="0" fill="var(--parallax-1)"/>
          <use xlink:href="#gentle-wave" x="48" y="3" fill="var(--parallax-2)"/>
          <use xlink:href="#gentle-wave" x="48" y="5" fill="var(--parallax-3)"/>
          <use xlink:href="#gentle-wave" x="48" y="7" fill="var(--parallax-4)"/>
        </g>
      </svg>
    </div>
    <navigation/>
  </section>
</template>

<style scoped>
/*背景*/
section.header-section {
  @apply 'h-full relative';
  animation: down-effect 1s;
}

/*内容*/
.header-banner span.mark {
  border-right: 2px solid var(--text-color);
  animation: blink 0.6s steps(40, end) infinite;
  transition: all var(--transition);
}

/* 滚动提示 */
.arrows-box:hover {
  animation: arrow-load 1s infinite ease-in-out forwards;
}

.scrolldown {
  border: calc(30px / 10) solid var(--theme-bg-reverse);
  transition: border var(--transition);
}

.scrolldown::before {
  @apply 'absolute bottom-30px left-1/2 w-6px h-6px -ml-3px bg-$theme-bg-reverse rounded-full border-box';
  content: "";
  animation: scrolldown-anim 2s infinite;
  box-shadow: 0px -5px 3px 1px #2a547066;
  transition: var(--theme-transition-bg);
}

@keyframes scrolldown-anim {
  0% {
    opacity: 0;
    height: 6px;
  }

  40% {
    opacity: 1;
    height: 10px;
  }

  80% {
    transform: translate(0, 20px);
    height: 10px;
    opacity: 0;
  }

  100% {
    height: 3px;
    opacity: 0;
  }
}

.chevrondown {
  @apply '-mt-6px relative w-10px h-10px inline-block';
  border: solid var(--theme-bg-reverse);
  border-width: 0 3px 3px 0;
  transform: rotate(45deg);
  transition: border var(--transition);
}

.chevrondown:nth-child(odd) {
  animation: pulse54012 500ms ease infinite alternate;
}

.chevrondown:nth-child(even) {
  animation: pulse54012 500ms ease infinite alternate 250ms;
}

@keyframes pulse54012 {
  from {
    opacity: 0;
  }

  to {
    opacity: 0.5;
  }
}

/* 滚动提示结束 */

/*水波效果*/
.wave-wrapper .wares {
  @apply 'absolute w-full bottom-0 theme-transition';
}

.parallax > use {
  animation: wares-move-forever 25s cubic-bezier(.55, .5, .45, .5) infinite;
  transition: var(--theme-transition-fill);
}

.parallax > use:nth-child(1) {
  animation-delay: -2s;
  animation-duration: 7s;
}

.parallax > use:nth-child(2) {
  animation-delay: -3s;
  animation-duration: 10s;
}

.parallax > use:nth-child(3) {
  animation-delay: -4s;
  animation-duration: 13s;
}

.parallax > use:nth-child(4) {
  animation-delay: -5s;
  animation-duration: 20s;
}

/*波浪动画*/
@keyframes wares-move-forever {
  0% {
    transform: translate3d(-90px, 0, 0);
  }
  100% {
    transform: translate3d(85px, 0, 0);
  }
}

/*光标动画*/
@keyframes blink {
  from,
  to {
    border-color: transparent;
  }
  50% {
    border-color: var(--text-color);
  }
}

/*箭头动画*/
@keyframes arrow-load {
  0% {
    bottom: 0;
  }
  50% {
    bottom: 30px
  }
  100% {
    bottom: 0
  }
}

.slideshow {
  @apply 'absolute top-0 left-0 w-full h-full list-none overflow-hidden';
}

.slideshow li span {
  @apply 'w-full h-full absolute top-0 left-0 text-transparent opacity-0 z-0';
  background-size: cover;
  background-position: 50% 50%;
  background-repeat: initial;
  -webkit-backface-visibility: hidden;
  backface-visibility: hidden;
  -webkit-animation: imageAnimation 36s linear infinite 0s;
  -moz-animation: imageAnimation 36s linear infinite 0s;
  animation: imageAnimation 36s linear infinite 0s;
}

.slideshow li:nth-child(1) span {
}

.slideshow li:nth-child(2) span {
  -webkit-animation-delay: 6s;
  -moz-animation-delay: 6s;
  animation-delay: 6s;
}

.slideshow li:nth-child(3) span {
  -webkit-animation-delay: 12s;
  -moz-animation-delay: 12s;
  animation-delay: 12s;
}

.slideshow li:nth-child(4) span {
  -webkit-animation-delay: 18s;
  -moz-animation-delay: 18s;
  animation-delay: 18s;
}

.slideshow li:nth-child(5) span {
  -webkit-animation-delay: 24s;
  -moz-animation-delay: 24s;
  animation-delay: 24s;
}

.slideshow li:nth-child(6) span {
  -webkit-animation-delay: 30s;
  -moz-animation-delay: 30s;
  animation-delay: 30s;
}

@keyframes imageAnimation {
  0% {
    opacity: 0;
    animation-timing-function: ease-in
  }
  8% {
    opacity: 1;
    transform: scale(1.05);
    animation-timing-function: ease-out
  }
  17% {
    opacity: 1;
    transform: scale(1.1) rotate(0deg)
  }
  25% {
    opacity: 0;
    transform: scale(1.1) rotate(0deg)
  }
  100% {
    opacity: 0
  }
}
</style>