<script setup lang='ts'>
import {Time, UploadPicture, MoreTwo} from '@icon-park/vue-next'
import {imageFallbackSrc} from '~/components/common/constant'
import {
  NSpace,
  NModal,
  NButton,
  NIcon,
  NImageGroup,
  NImage,
  NPopover,
  NSpin,
  NText,
  NInput,
  NSelect,
  NScrollbar,
  NUpload,
  NAlert,
  NSkeleton,
  NGridItem,
  NGrid,
} from "naive-ui"
import {
  showPicturesModel,
  currentClickAlbumInfo,
} from './data/useAlbum'

import {
  pictureData,
  hasPictureData,
  isEditPictureName,
  pictureNameInputRef,
  clickPictureNameHandle,
  pictureNameChangeHandle,
  showMovePictureAlbumModel,
  generateAlbumOptionHandel,
  movePictureAlbumHandle,
  albumOptions,
  albumValue,
  deletePictureHandle,
  fileListLength,
  fileUrlList,
  showUploadPictureModel,
  uploadHandleChange,
  uploadPictureHandle,
  refreshPictureData,
  morePictureHandle,
  showMoreBtn,
  closeModel,
  moreLoading,
  disabledFlag
} from './data/usePicture'
import {UploadInst} from "naive-ui";
import {ref} from "vue";

const uploadRef = ref<UploadInst | null>(null)

// 点击上传图片
const uploadHandleClick = () => {
  fileUrlList.value.length = 0
  uploadRef.value?.submit()
}

</script>

<template>
  <n-space>
    <n-modal
        v-model:show="showPicturesModel"
        preset="card"
        :style="{width: '100vw', height: '100vh'}"
        :title="currentClickAlbumInfo.albumName"
        size="small"
        :bordered="false"
        :segmented="{content: 'soft',footer: 'soft'}"
        :mask-closable="false"
        @after-leave="closeModel"
        :content-style="{overflow: 'auto'}"
    >
      <template #header-extra>
        <n-space align="center">
          <n-button tertiary size="small" type="info" @click="refreshPictureData" :disabled="disabledFlag">
            <template #icon>
              <n-icon>
                <UploadPicture/>
              </n-icon>
            </template>
            刷新
          </n-button>
          <n-button tertiary size="small" type="warning" @click="showUploadPictureModel = true"
                    :disabled="disabledFlag">
            <template #icon>
              <n-icon>
                <UploadPicture/>
              </n-icon>
            </template>
            添加
          </n-button>
        </n-space>
      </template>
      <n-image-group>
        <div v-if="pictureData.length !== 0" id="picture-scroll-container" class="flex flex-wrap gap-10px">
          <n-popover trigger="hover" v-for="picture in pictureData" :key="picture.id">
            <template #trigger>
              <n-image
                  :style="{ height: '120px', maxWidth: '300px', justifyContent: 'center', alignContent: 'center', borderRadius: '5px',flexGrow: '1'}"
                  object-fit="cover"
                  :src="picture.pictureSrc"
                  :fallback-src="imageFallbackSrc"
                  :showToolbarTooltip="true"
                  :lazy="true"
                  :img-props="{style: {width: '100%'}}"
                  :intersection-observer-options="{root: '#picture-scroll-container'}"
              >
                <template #placeholder>
                  <n-spin size="small"/>
                </template>
              </n-image>
            </template>
            <n-space vertical>
              <div @click="clickPictureNameHandle" class="min-w-120px">
                <n-text v-if="isEditPictureName">{{ picture.pictureName }}</n-text>
                <n-input v-else autofocus ref="pictureNameInputRef" size="small" v-model:value="picture.pictureName"
                         @blur="isEditPictureName = true" @change="pictureNameChangeHandle(picture)"/>
              </div>
              <n-space align="center" justify="space-between">
                <n-space>
                  <n-button size="small" text type="warning" @click="generateAlbumOptionHandel(picture)"
                            :disabled="disabledFlag">移动
                  </n-button>
                  <n-button size="small" text type="error" @click="deletePictureHandle(picture)"
                            :disabled="disabledFlag">删除
                  </n-button>
                </n-space>
                <n-space>
                  <n-popover trigger="hover" :style="{width: '155px'}">
                    <template #trigger>
                      <n-icon>
                        <Time/>
                      </n-icon>
                    </template>
                    <span class="text-12px">{{ picture.updateTime }}</span>
                  </n-popover>
                </n-space>
              </n-space>
            </n-space>
          </n-popover>
          <n-space vertical justify="center" v-if="showMoreBtn">
            <n-button text type="info" @click="morePictureHandle" :loading="moreLoading" :disabled="disabledFlag">
              <template #icon>
                <n-icon>
                  <MoreTwo/>
                </n-icon>
              </template>
              更多
            </n-button>
          </n-space>
          <n-modal v-model:show="showMovePictureAlbumModel" preset="dialog" title="Dialog" :mask-closable="false">
            <template #header>
              <div>移动相册</div>
            </template>
            <n-select v-model:value="albumValue" size="small" :options="albumOptions" :consistent-menu-width="false"/>
            <template #action>
              <n-button @click="movePictureAlbumHandle" tertiary type="info" size="small" :disabled="disabledFlag">
                移动
              </n-button>
            </template>
          </n-modal>
        </div>
        <n-space v-else vertical>
          <n-space v-if="hasPictureData" justify="center" align="center">
            <n-alert :show-icon="false" type="info">
              没有图片可看
            </n-alert>
          </n-space>
          <n-scrollbar style="height: 80vh" v-else>
            <n-grid cols="3 s:5 m:6 l:7 xl:8 2xl:9" style="gap: 8px;" responsive="screen">
              <n-grid-item v-for="num in 20" :key="num">
                <n-skeleton height="120px"/>
              </n-grid-item>
            </n-grid>
          </n-scrollbar>
        </n-space>
      </n-image-group>
      <template #footer>
        <n-space justify="space-between">
          <n-space :style="{fontSize: '12px'}">
            <n-text type="info">创建时间：{{ currentClickAlbumInfo.createTime }}</n-text>
            <n-text type="info">更新时间：{{ currentClickAlbumInfo.updateTime }}</n-text>
          </n-space>
          <n-text>共 {{ currentClickAlbumInfo.pictureCount }} 张</n-text>
        </n-space>
      </template>
    </n-modal>
    <n-modal v-model:show="showUploadPictureModel" preset="dialog" title="Dialog" :mask-closable="false"
             style="width: 375px">
      <template #header>
        <n-text>新增图片</n-text>
      </template>
      <n-scrollbar style="max-height: 100px;">
        <n-upload
            ref="uploadRef"
            list-type="image-card"
            :default-upload="false"
            multiple
            @change="uploadHandleChange"
            :custom-request="uploadPictureHandle"
        >
          点击上传
        </n-upload>
      </n-scrollbar>
      <template #action>
        <n-button size="small" ertiary type="info" :disabled="!fileListLength" @click="uploadHandleClick">上传
        </n-button>
      </template>
    </n-modal>
  </n-space>
</template>

<style>

</style>