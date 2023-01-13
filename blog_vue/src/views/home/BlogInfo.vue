<script setup lang='ts'>
import {
  Book,
  CategoryManagement,
  Bookmark,
  MessageEmoji,
  Wechat,
  TencentQq,
  Github,
  Announcement,
  Info
} from '@icon-park/vue-next'
import BoxComponent from '@/components/BoxComponent.vue'
import {onMounted, ref} from "vue";
import {handlerDateDurationCurrent, parseComment} from "@/utils/utils";
import {useWebsiteInfoStore} from '@/store'
import {storeToRefs} from "pinia";
import {loadingImg} from "@/constant"

const websiteInfoStore = useWebsiteInfoStore()
const {websiteInfo} = storeToRefs(websiteInfoStore)

const timer = ref("")
onMounted(() => {
  setInterval(() => {
    timer.value = handlerDateDurationCurrent(parseInt(websiteInfo.value?.websiteCreateTime as string));
  }, 1000)
})

</script>

<template>
  <BoxComponent class="blog-info info-content-item flex flex-col items-center gap-12px">
    <div class="author-name w-full h-35px text-center leading-35px">
      <span class="text-20px">{{ websiteInfo?.websiteAuthor }}</span>
      <hr class="hr-edge-weak">
    </div>
    <div class="w-150px h-150px shadow rounded-full flex justify-center items-center mt-5px shake"
         style="transition: var(--theme-transition-bg), var(--theme-transition-shadow)">
      <div class="w-140px h-140px shadow-inset rounded-full flex justify-center items-center"
           style="box-shadow: var(--theme-transition-bg)">
        <div class="w-126px h-126px ">
          <img class="avatar rounded-full w-full h-full"
               :src="websiteInfo?.websiteAvatar" alt="">
        </div>
      </div>
    </div>
    <div class="w-full flex justify-evenly">
          <span class="flex flex-col justify-center items-center rounded-6px p-3px shadow">
            <span class="flex items-center gap-3px"><Book/>文章</span>
            <span>{{ websiteInfo?.articleCount}}</span>
          </span>
      <span class="flex flex-col justify-center items-center rounded-6px p-3px shadow">
            <span class="flex items-center gap-3px"><CategoryManagement/>分类</span>
            <span>{{ websiteInfo?.categoryCount }}</span>
          </span>
      <span class="flex flex-col justify-center items-center rounded-6px p-3px shadow">
            <span class="flex items-center gap-3px"><Bookmark/>标签</span>
            <span>{{websiteInfo?.tagCount}}</span>
          </span>
    </div>
    <div class="mb-15px break-all px-15px flex justify-center">
      <p class="whitespace-pre-line break-all" v-html="parseComment(websiteInfo?.websiteIntro, '20', '20')"></p>
    </div>
  </BoxComponent>
  <BoxComponent class="info-content-item px-10px py-5px">
    <span class="text-16px flex items-center gap-8px"><MessageEmoji size="20"/>社交</span>
    <div class="social-contact relative flex justify-center gap-12px mt-10px mb-5px">
          <span :data-attr="websiteInfo?.wechat" class="social-contact-wechat w-35px h-35px rounded-full flex justify-center items-center shadow">
              <Wechat size="24"/>
          </span>
      <span class="w-35px h-35px rounded-full flex justify-center items-center shadow">
            <a :href="`https://wpa.qq.com/msgrd?v=3&uin=${1473018671}&site=qq&menu=yes`" target="_blank">
              <TencentQq size="24"/>
            </a>
          </span>
      <span class="w-35px h-35px rounded-full flex justify-center items-center shadow">
            <a href="https://github.com/apecodex" target="_blank">
              <Github size="24"/>
            </a>
          </span>
    </div>
  </BoxComponent>
  <BoxComponent class="info-content-item px-10px py-5px">
    <span class="text-16px flex items-center gap-8px"><Announcement size="20"/>公告</span>
    <div class="my-5px">
      <p class="whitespace-pre-line break-all" v-html="parseComment(websiteInfo?.websiteNotice, '20', '20')"></p>
    </div>
  </BoxComponent>
  <BoxComponent class="info-content-item px-10px py-5px">
    <span class="text-16px flex items-center gap-8px"><Info size="20"/>网站资讯</span>
    <div class="my-5px flex flex-col gap-5px">
      <p class="break-all flex justify-between">
        <span>已运行：</span>
        <span>{{ timer }}</span>
      </p>
      <p class="break-all flex justify-between">
        <span>本站访客数：</span>
        <span>{{ websiteInfo?.viewsCount }}</span>
      </p>
      <p class="break-all flex justify-between">
        <span>本站总访问量：</span>
        <span>{{ websiteInfo?.onlyViewCount }}</span>
      </p>
    </div>
  </BoxComponent>
</template>

<style scoped>

.info-content-item {
  animation: flipInX 1s ease-in;
}

.author-name > span:after {
  content: "✿";
  padding-left: 8px;
}

.author-name > span:before {
  content: "✿";
  padding-right: 8px;
}

.blog-info:hover .author-name > span:after {
  color: var(--hover-color);
}

.blog-info:hover .author-name > span:before {
  color: var(--hover-color);
}

.social-contact span:hover {
  box-shadow: var(--theme-shadow-inset) !important;
}

.social-contact-wechat:after {
  content: attr(data-attr);
  position: absolute;
  top: 40px;
  height: 30px;
  line-height: 30px;
  padding: 0 5px;
  border-radius: 6px;
  background-color: var(--theme-bg);
  box-shadow: var(--theme-shadow);
  color: var(--theme-bg-reverse);
  opacity: 0;
  transition: opacity .3s;
}

.social-contact-wechat:hover:after {
  opacity: 1;
}
</style>