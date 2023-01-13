<script setup lang='ts'>
import BoxComponent from "@/components/BoxComponent.vue"
import Skeleton from "@/components/skeleton/Skeleton.vue"
import {loadingImg} from "@/constant"
import {
  pagination,
  albumsData,
  createAlbumsData,
  moreAlbumHandle
} from "./AlbumHooks"
import {onMounted} from "vue";

onMounted(() => {
  createAlbumsData({size: pagination.size, current: pagination.current});
})

</script>

<template>
  <BoxComponent class="p-10px" v-if="albumsData">
    <div class="text-center text-24px text-shadow-xl">相册（{{ albumsData.count }}）</div>
    <hr class="hr-edge-weak my-20px">
    <div class="albums-wrapper grid gap-10px" v-if="albumsData.count !== 0">
      <div class="albums-inner" v-for="album of albumsData.recordList" :key="album.id">
        <router-link :to="`/album/${album.id}/pictures`"
                     class="albums-cover overflow-hidden relative rounded-6px flex justify-center items-center h-130px mb-5px"
                     :data-count="`${album.pictureCount}张`" title="点击查看相册">
          <img v-lazy :data-src="album.albumCover" :src="loadingImg" alt="" class="w-full h-full">
          <div class="w-full absolute bottom-0 px-5px">
            <span class="text-12px cursor-default">最后更新：{{ album.updateTime }}</span>
          </div>
        </router-link>
        <router-link :to="`/album/${album.id}/pictures`" class="text-16px hover:(text-$hover-color2)">{{
            album.albumName
          }}
        </router-link>
      </div>
      <div class="more-album flex justify-center items-center" v-if="albumsData.recordList.length !== albumsData.count">
        <button class="px-10px h-30px bg-$theme-bg-reverse text-$text-color-reverse rounded-6px"
                @click="moreAlbumHandle">查看更多
        </button>
      </div>
    </div>
    <p class="text-18px text-center" v-else>暂无相册</p>
  </BoxComponent>
  <BoxComponent v-else class="p-10px">
    <div class="flex justify-center">
      <Skeleton customize-class="!w-120px h-25px"/>
    </div>
    <hr class="hr-edge-weak my-10px">
    <div class="albums-wrapper grid gap-10px">
      <Skeleton v-for="i in 8" customize-class="article-item relative !w-full h-200px"/>
    </div>
  </BoxComponent>
</template>

<style scoped>
.albums-wrapper {
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
}

.albums-inner .albums-cover img {
  @apply 'opacity-20 object-cover';
  transition: transform .3s ease-in, opacity .3s ease-in;
}

.albums-inner .albums-cover img:hover {
  @apply 'opacity-100';
  transform: scale(1.2);
}

.albums-inner .albums-cover::before {
  @apply 'absolute text-18px';
  content: attr(data-count);
}

/* 查看更多相册 */
.more-album button {
  transition: var(--theme-transition);
}

.more-album button:hover {
  @apply 'bg-$theme-bg text-$text-color';
  box-shadow: var(--theme-shadow);
}
</style>