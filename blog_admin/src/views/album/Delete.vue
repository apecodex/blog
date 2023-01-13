<script setup lang='ts'>
import {imageFallbackSrc} from '~/components/common/constant'
import {
  pictureData,
  hasPictureData,
  createDeletePictureData,
  recoverPictureHandle,
  deletePictureHandle
} from './data/useDelete'
import {
  NSpace,
    NImageGroup,
    NPopover,
    NImage,
    NSpin,
    NGradientText,
    NText,
    NButton,
    NAlert,
    NScrollbar,
    NGrid,
    NGridItem,
    NSkeleton
} from "naive-ui"
import {onMounted} from "vue";

onMounted(() => {
  pictureData.value.length = 0;
  createDeletePictureData({current: 1, size: 1000});
})

</script>

<template>
  <n-space vertical>
    <n-space v-if="pictureData.length !== 0">
      <n-image-group>
        <div id="deletePicture-scroll-container" class="flex flex-wrap gap-10px">
          <n-space vertical v-for="picture in pictureData" :key="picture.id" class="flex-grow max-w-400px" :item-style="{width: '100%'}">
            <n-popover trigger="hover">
              <template #trigger>
                <n-image
                    :style="{ height: '120px',width: '100%', justifyContent: 'center', alignContent: 'center', borderRadius: '5px'}"
                    object-fit="cover"
                    :src="picture.pictureSrc"
                    :fallback-src="imageFallbackSrc"
                    :showToolbarTooltip="true"
                    :lazy="true"
                    :img-props="{style: {width: '100%'}}"
                    :intersection-observer-options="{root: '#deletePicture-scroll-container'}"
                >
                  <template #placeholder>
                    <n-spin size="small"/>
                  </template>
                </n-image>
              </template>
              <n-space vertical>
                <n-gradient-text type="info">{{ picture.pictureName }}</n-gradient-text>
                <n-text :style="{fontSize: '12px'}">删除时间：{{ picture.updateTime }}</n-text>
              </n-space>
            </n-popover>
            <n-space justify="center">
              <n-button size="small" text type="info" @click="recoverPictureHandle(picture)">恢复</n-button>
              <n-button size="small" text type="error" @click="deletePictureHandle(picture)">彻底删除</n-button>
            </n-space>
          </n-space>
        </div>
      </n-image-group>
    </n-space>
    <n-space vertical v-else>
      <n-space v-if="hasPictureData" justify="center" align="center">
        <n-alert :show-icon="false" type="info">
          回收站是空的
        </n-alert>
      </n-space>
      <n-scrollbar v-else style="height: 80vh">
        <n-grid cols="3 s:5 m:6 l:7 xl:8 2xl:9" style="gap: 8px;" responsive="screen">
          <n-grid-item v-for="num in 20" :key="num">
            <n-space vertical>
              <n-skeleton height="120px"/>
              <n-skeleton height="21px"/>
            </n-space>
          </n-grid-item>
        </n-grid>
      </n-scrollbar>
    </n-space>
  </n-space>
</template>

<style scoped>

</style>