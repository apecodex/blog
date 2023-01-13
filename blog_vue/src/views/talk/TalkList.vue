<script setup lang='ts'>
import  BoxComponent from "@/components/BoxComponent.vue"
import Skeleton from "@/components/skeleton/Skeleton.vue";
import {Like, Comments} from "@icon-park/vue-next"
import {onMounted} from "vue";
import {loadingImg} from "@/constant"
import {parseComment} from "@/utils/utils"
import {
  pagination,
  talksData,
  createTalksData,
  moreTalkHandle
} from "./TalkListHooks"

onMounted(() => {
  pagination.current = 1;
  createTalksData(pagination)
})
</script>

<template>
  <BoxComponent class="p-10px" v-if="talksData">
    <div class="flex flex-col gap-20px" v-if="talksData.count">
      <div class="text-center">
        <h2 class="text-24px text-shadow-xl">说说 ({{ talksData.count }})</h2>
        <hr class="hr-edge-weak mt-20px">
      </div>
      <div class="flex gap-10px min-h-68px shadow py-5px pr-5px rounded-6px"
           v-for="talk of talksData.recordList"
           :key="talk.id">
        <div class="flex flex-col justify-between items-end w-60px">
          <div class="text-18px">
            <div>{{ talk.createTime.getDate() }}/<span class="text-12px">{{ talk.createTime.getMonth() + 1 }}月</span>
            </div>
            <div class="text-12px text-right">{{ talk.createTime.getFullYear() }}</div>
          </div>
          <div class="text-$hover-color2 text-12px shadow rounded-6px py-3px px-5px" v-if="talk.isTop">顶置
          </div>
        </div>
        <router-link title="查看说说" :to="`/talk/${talk.id}`"
                     class="flex w-92px h-92px flex-wrap gap-2px <md:(w-72px h-72px)" v-if="talk.src.length !== 0">
          <div class="w-45px h-45px flex-grow <md:(w-35px h-35px) overflow-hidden"
               :class="{'!h-full': talk.src.length <= 2}"
               v-for="img of talk.src">
            <img v-lazy :data-src="img" :src="loadingImg" alt="" class="w-full h-full object-cover zoomImg rounded-1px">
          </div>
        </router-link>
        <div class="flex flex-col justify-between flex-1 gap-10px">
          <router-link :to="`/talk/${talk.id}`" title="查看说说">
            <p class="whitespace-pre-wrap talk-content leading-[1.2] h-full overflow-hidden overflow-ellipsis"
               v-html="parseComment(talk.content, '24', '24')"></p>
          </router-link>
          <div class="flex items-center gap-10px">
            <div class="text-12px text-$text-color2" style="transition: var(--theme-transition-color)"
                 v-if="talk.src.length !== 0">共{{ talk.src.length }}张
            </div>
            <router-link title="查看说说评论" :to="`/talk/${talk.id}`" class="flex items-center gap-5px cursor-pointer">
              <Like :theme="talk.isLike ? 'filled':''" :class="{'text-red-600': talk.isLike}"/>
              <span class="text-12px">{{ talk.likeCount }}</span></router-link>
            <router-link title="查看说说评论" :to="`/talk/${talk.id}`" class="flex items-center gap-5px">
              <Comments/>
              <span class="text-12px">{{ talk.commentCount }}</span></router-link>
          </div>
        </div>
      </div>
      <div class="flex flex-col justify-center" v-if="talksData.recordList.length !== talksData.count">
        <hr class="hr-twill">
        <div class="flex justify-center">
          <button
              class=" py-3px px-6px outline-none rounded-6px mt-8px shadow bg-$theme-bg hover:(bg-$theme-bg-reverse text-$theme-bg)"
              style="transition: var(--theme-transition-shadow), var(--theme-transition-bg)" @click="moreTalkHandle">
            查看剩余的{{ talksData.count - talksData.recordList.length }}条
          </button>
        </div>
      </div>
      <div v-else>
        <hr class="hr-twill">
        <div class="flex justify-center">
          <span>⁽⁽ଘ(ˊᵕˋ)ଓ⁾⁾ 加载好了哦~</span>
        </div>
      </div>
    </div>
    <div class="flex flex-col" v-else>
      <div class=" flex justify-center">
        <Skeleton customize-class="!h-25px"/>
      </div>
      <div class="mt-30px flex flex-col gap-15px">
        <div class="flex gap-15px" v-for="i in 5" :key="i">
          <div class="flex flex-col justify-between">
            <Skeleton customize-class="!h-52px !w-60px <md:(!h-42px)"/>
            <Skeleton customize-class="!h-22px !w-60px <md:(!h-12px)"/>
          </div>
          <Skeleton customize-class="!h-100px !w-100px <md:(!h-70px !w-70px)"/>
          <Skeleton customize-class="!h-100px flex-1 <md:(!h-70px)"/>
        </div>
      </div>
    </div>
  </BoxComponent>
  <BoxComponent class="p-10px flex flex-col" v-else>
    <p class="text-center text-18px">博主还没有想好要分享点啥呢？~</p>
  </BoxComponent>
</template>

<style scoped>
.talk-content {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>