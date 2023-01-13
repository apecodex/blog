<script setup lang='ts'>
import BoxComponent from "@/components/BoxComponent.vue"
import PreviewPicture from "@/components/preview/PreviewPicture.vue"
import ScrollLoad from "@/components/scroll/ScrollLoad.vue"
import {onMounted, reactive, Ref, ref, watch} from "vue";
import {useRoute} from "vue-router"
import {Time, UpdateRotation} from "@icon-park/vue-next"
import {loadingImg} from "@/constant"
import {
  pagination,
  picturesData,
  createPicturesData,
  updatePicturesData
} from "./PictureHooks"
import Skeleton from "@/components/skeleton/Skeleton.vue";

const route = useRoute()

const pictureRef: Ref<HTMLElement | null> = ref(null)
const currentPicture = reactive({
  image: "",
  current: 0,
  maxCount: 1,
  width: 0,
  height: 0,
  pictureName: ""
})

watch(() => route.params?.albumId, () => {
  const albumId = route.params?.albumId as string | null;
  if (albumId) {
    pagination.albumPath = albumId
    createPicturesData(pagination)
  }
})

onMounted(() => {
  const albumId = route.params?.albumId as string | null;
  if (albumId) {
    pagination.albumPath = albumId
    createPicturesData(pagination)
  }
})

const loadingHandle = () => {
  if (picturesData.value?.recordList.length !== picturesData.value?.count) {
    updatePicturesData(pagination)
  }
}

// 点击预览图片
const previewPictureHandle = (currentIndex: number) => {
  const picture: PictureEntity = picturesData.value?.recordList[currentIndex] as PictureEntity;
  currentPicture.image = picture.pictureSrc;
  currentPicture.width = picture.width as number;
  currentPicture.height = picture.height as number;
  currentPicture.pictureName = picture.pictureName;
  currentPicture.current = currentIndex;
  currentPicture.maxCount = picturesData.value?.recordList.length as number
  document.body.style.overflow = 'hidden'
}

// 上一张图片
const prevHandle = () => {
  currentPicture.current--;
  const picture: PictureEntity = picturesData.value?.recordList[currentPicture.current] as PictureEntity;
  currentPicture.image = picture.pictureSrc;
  currentPicture.width = picture.width as number;
  currentPicture.height = picture.height as number;
  currentPicture.pictureName = picture.pictureName;
}

// 下一张图片
const nextHandle = () => {
  currentPicture.current++;
  const picture: PictureEntity = picturesData.value?.recordList[currentPicture.current] as PictureEntity;
  currentPicture.image = picture.pictureSrc;
  currentPicture.width = picture.width as number;
  currentPicture.height = picture.height as number;
  currentPicture.pictureName = picture.pictureName;
}
</script>

<template>
  <BoxComponent class="p-10px" v-if="picturesData">
    <div class="flex items-center flex-col gap-10px">
      <h2 class="text-24px text-shadow-xl">{{ picturesData.data.albumName }}</h2>
      <p class="text-$hover-color2">{{ picturesData.data.albumDesc }}</p>
      <div class="text-12px flex justify-center gap-15px flex-wrap">
        <span class="flex gap-5px shadow py-3px px-6px rounded-6px" title="创建时间"><Time size="15"/> {{
            picturesData.data.createTime.substring(0, picturesData.data.createTime.lastIndexOf(":"))
          }}</span>
        <span class="flex gap-5px shadow py-3px px-6px rounded-6px" title="更新时间"><UpdateRotation size="15"/> {{
            picturesData.data.updateTime.substring(0, picturesData.data.updateTime.lastIndexOf(":"))
          }}</span>
      </div>
    </div>
    <hr class="hr-edge-weak my-20px">
    <template v-if="picturesData.count !== 0">
      <div ref="pictureRef" class="picture-wrapper">
        <div class="picture-item" v-for="(picture, index) of picturesData.recordList" :key="index"
             @click="previewPictureHandle(index)">
          <img v-lazy :data-src="picture.pictureSrc" :src="loadingImg" alt="">
          <div class="picture-info">
            <span class="text-12px">{{ picture.pictureName }}</span>
          </div>
        </div>
      </div>
      <PreviewPicture :picture="currentPicture" @next="nextHandle" @prev="prevHandle"/>
      <ScrollLoad :current-count="picturesData.recordList.length" :total-count="picturesData.count"
                  @update="loadingHandle"/>
    </template>
    <p class="text-center text-18px" v-else>暂无图片</p>
  </BoxComponent>
  <BoxComponent class="p-10px" v-else>
    <div class="flex flex-col items-center gap-15px">
      <Skeleton customize-class="!h-30px"/>
      <Skeleton customize-class="!w-260px !h-20px"/>
      <div class="flex gap-15px">
        <Skeleton customize-class="!h-15px"/>
        <Skeleton customize-class="!h-15px"/>
      </div>
    </div>
    <div class="grid gap-15px px-5px mt-25px" style="grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));">
      <Skeleton v-for="i in 12" customize-class="article-item relative !w-full h-160px"/>
    </div>
  </BoxComponent>
</template>

<style scoped>
.picture-wrapper {
  @apply 'flex flex-wrap gap-10px';
}

.picture-item {
  @apply 'relative flex-grow rounded-6px overflow-hidden cursor-pointer max-w-400px';
  counter-increment: item-counter;
}

.picture-item::after {
  @apply 'absolute top-2px left-2px px-5px bg-$text-color-reverse rounded-full shadow';
  content: counter(item-counter);
  transition: var(--theme-transition-bg);
}

.picture-item img {
  @apply 'w-full h-200px object-cover duration-300 <sm:(h-100px)';
}

.picture-info {
  @apply 'absolute top-0 w-full h-full flex justify-center items-center opacity-0';
}

.picture-item:hover img {
  @apply 'opacity-20';
  transform: scale(1.2);
}

.picture-item:hover .picture-info {
  opacity: 1;
}
</style>