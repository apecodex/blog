<script setup lang='ts'>
import {useWebsiteInfoStore} from '@/store'
import {storeToRefs} from "pinia";

const websiteInfoStore = useWebsiteInfoStore()
const {websiteInfo} = storeToRefs(websiteInfoStore)

</script>

<template>
  <div
    class="footer-wrapper relative w-full h-120px mt-30px text-$theme-bg flex flex-col justify-center items-center pt-20px">
    <div class="mb-10px">Copyright © {{ new Date(websiteInfo?.websiteCreateTime * 1000).getFullYear() }} - 至今 | By <a href="https://github.com/apecodex">apecode.</a></div>
    <div class="flex w-full justify-center items-center <sm:(flex-col items-center)">
      <div>
        <a target="_blank" href="#"
           class="flex items-center">
          <img src="@/assets/imgs/public_security_record.png"/>
          <p class="ml-2px text-12px">公安备案</p></a>
      </div>
      <div class="ml-10px">
        <a href="https://beian.miit.gov.cn" target="_blank" class="outline-none">
          <p class="text-12px">{{ websiteInfo?.websiteRecordNo }}</p>
        </a>
      </div>
    </div>
  </div>
</template>

<style scoped>
.footer-wrapper {
  @apply 'bg-$theme-bg-reverse';
  transition: var(--theme-transition-bg);
}

.footer-wrapper::before {
  content: '';
  @apply 'absolute top-0 left-0 w-full h-24px';
  background: linear-gradient(-135deg, var(--theme-bg) 12px, transparent 0%), linear-gradient(135deg, var(--theme-bg) 12px, transparent 0%);
  background-size: 24px;
}
</style>