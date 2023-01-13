<script setup lang='ts'>
import {CheckOne, CloseOne, EditTwo, Delete, Newlybuild} from '@icon-park/vue-next'
import {imageFallbackSrc} from '~/components/common/constant'
import {
  createAlbumData,
  albumPagination,
  albumData,
  hasAlbumData,
  searchData,
  searchAlbumHandle,
  albumCoverFile,
  openPictureHandle,
  showUpdateAlbumDrawer,
  showNewAlbumDrawer,
  showUpdateAlbumDrawerHandle,
  removeAlbumCoverHandle,
  albumFormInfo,
  uploadAlbumCoverHandel,
  updateAlbumHandle,
  newAlbumHandle,
  deleteAlbumHandle,
  closeAlbumModel,
  checkAlbumInfo
} from './data/useAlbum'
import {
  NSpace,
  NInputGroup,
  NInput,
  NButton,
  NIcon,
  NGrid,
  NGridItem,
  NCard,
  NImage,
  NSpin,
  NScrollbar,
  NEllipsis,
  NText,
  NPopover,
  NAvatar,
  NAlert,
  NSkeleton,
  NSwitch,
  NTag,
  NDivider,
  NUpload,
  NDrawerContent,
  NDrawer,
} from "naive-ui";
import DescSetting from "~/components/DescSetting.vue"
import Picture from "~/views/album/Picture.vue";
import {onMounted} from "vue";

onMounted(() => {
  albumData.value.length = 0
  createAlbumData({current: albumPagination.value.page.value, size: albumPagination.value.pageSize.value})
})

</script>

<template>
  <n-space vertical>
    <n-space justify="space-between">
      <n-input-group>
        <n-input placeholder="相册名称" clearable size="small" v-model:value="searchData.keywords"/>
        <n-button type="primary" size="small" ghost @click="searchAlbumHandle">搜索</n-button>
      </n-input-group>
      <n-button tertiary round strong type="info" size="small" @click="showNewAlbumDrawer = true">
        <template #icon>
          <n-icon>
            <Newlybuild/>
          </n-icon>
        </template>
        新增相册
      </n-button>
    </n-space>
    <n-space vertical>
      <n-grid cols="2 s:4 m:5 l:5 xl:6 2xl:7" style="gap: 8px;" responsive="screen" id="album-scroll-container"
              v-if="albumData.length !== 0">
        <n-grid-item v-for="album in albumData" :key="album.id">
          <n-card :title="album.albumName" size="small" hoverable embedded content-style="padding: 3px"
                  header-style="padding: 0 3px" footer-style="padding: 0 3px">
            <template #cover>
              <n-image
                  :style="{width: '100vw', height: '120px', justifyContent: 'center'}"
                  object-fit="cover"
                  :src="album.albumCover"
                  :fallback-src="imageFallbackSrc"
                  :intersection-observer-options="{root: '#album-scroll-container'}"
                  :showToolbarTooltip="true"
                  :lazy="true"
              >
                <template #placeholder>
                  <n-spin size="small"/>
                </template>
              </n-image>
            </template>
            <n-scrollbar style="height: 80px">
              <n-ellipsis expand-trigger="click" line-clamp="3" :tooltip="false">
                <n-text depth="3">
                  {{ album.albumDesc.length === 0 ? "这个相册没有任何介绍~" : album.albumDesc }}
                </n-text>
              </n-ellipsis>
            </n-scrollbar>
            <template #footer>
              <n-space align="center">
                <n-popover trigger="hover" :style="{padding: '5px'}">
                  <template #trigger>
                    <n-avatar
                        size="small"
                        :src="album.user.avatar"
                    />
                  </template>
                  <n-space vertical style="font-size: 12px">
                    <n-text>UID: {{ album.user.uid }}</n-text>
                    <n-text>昵称: {{ album.user.nickname }}</n-text>
                  </n-space>
                </n-popover>
                <n-tag :type="album.status ? 'success':'warning'" size="small">
                  {{ album.status ? "公开" : "私密" }}
                  <template #icon>
                    <CheckOne v-if="album.status"/>
                    <CloseOne v-else/>
                  </template>
                </n-tag>
                <n-popover trigger="hover">
                  <template #trigger>
                    <n-button text style="font-size: 18px" type="info" @click="showUpdateAlbumDrawerHandle(album)">
                      <n-icon>
                        <EditTwo/>
                      </n-icon>
                    </n-button>
                  </template>
                  <span>修改相册</span>
                </n-popover>
                <n-popover trigger="hover">
                  <template #trigger>
                    <n-button text style="font-size: 18px" type="error" @click="deleteAlbumHandle(album)">
                      <n-icon>
                        <Delete/>
                      </n-icon>
                    </n-button>
                  </template>
                  <span>删除相册</span>
                </n-popover>
              </n-space>
            </template>
            <template #action>
              <n-button text size="large" class="w-full text-14px" @click="openPictureHandle(album)">
                查看（{{ album.pictureCount }}张）
              </n-button>
            </template>
          </n-card>
        </n-grid-item>
      </n-grid>
      <n-space vertical v-else justify="center">
        <n-space v-if="hasAlbumData" justify="center">
          <n-alert :show-icon="false" type="info" justify="center" align="center">
            暂无相册
          </n-alert>
        </n-space>
        <n-grid v-else cols="2 s:4 m:5 l:5 xl:6 2xl:7" style="gap: 8px;" responsive="screen">
          <n-grid-item v-for="num in 10" :key="num">
            <n-skeleton height="120px"/>
            <n-skeleton text style="width: 40%"/>
            <n-skeleton text :repeat="3"/>
            <n-skeleton text style="width: 50%"/>
            <n-skeleton text height="47px"/>
          </n-grid-item>
        </n-grid>
      </n-space>
      <Picture/>
    </n-space>
    <n-drawer v-model:show="showUpdateAlbumDrawer" @after-leave="closeAlbumModel" :width="300">
      <n-drawer-content closable :native-scrollbar="false">
        <template #header>
          <n-text>修改相册</n-text>
        </template>
        <n-space vertical>
          <div>
            <n-input v-model:value="albumFormInfo.albumName.content" type="text" size="small"
                     placeholder="相册名称" clearable/>
            <span class="text-tip" v-show="albumFormInfo.albumName.status">相册名称不可为空</span>
          </div>
          <n-input
              v-model:value="albumFormInfo.albumDesc"
              placeholder="包含的故事..."
              clearable
              type="textarea"
              size="small"
              :autosize="{minRows: 3,maxRows: 3}"
          />
          <desc-setting title="状态">
            <n-switch :round="false" v-model:value="albumFormInfo.status">
              <template #checked>
                公开
              </template>
              <template #unchecked>
                私密
              </template>
            </n-switch>
          </desc-setting>
          <n-divider dashed>
            相册封面
          </n-divider>
          <n-space vertical align="center">
            <n-upload
                list-type="image-card"
                :default-file-list="albumCoverFile"
                :custom-request="uploadAlbumCoverHandel"
                @remove="removeAlbumCoverHandle"
                :max="1"
            >
              点击上传
            </n-upload>
            <span class="text-tip" v-show="albumFormInfo.albumCover.status">相册封面不可为空</span>
          </n-space>
        </n-space>
        <template #footer>
          <n-button @click="updateAlbumHandle" :disabled="checkAlbumInfo">保存</n-button>
        </template>
      </n-drawer-content>
    </n-drawer>
    <n-drawer v-model:show="showNewAlbumDrawer" @after-leave="closeAlbumModel" :width="300">
      <n-drawer-content closable :native-scrollbar="false">
        <template #header>
          <n-text>新增相册</n-text>
        </template>
        <n-space vertical>
          <div>
            <n-input v-model:value="albumFormInfo.albumName.content" type="text" size="small"
                     placeholder="相册名称" clearable/>
            <span class="text-tip" v-show="albumFormInfo.albumName.status">相册名称不可为空</span>
          </div>
          <n-input
              v-model:value="albumFormInfo.albumDesc"
              placeholder="包含的故事..."
              clearable
              type="textarea"
              size="small"
              :autosize="{minRows: 3,maxRows: 3}"
          />
          <desc-setting title="状态">
            <n-switch :round="false" v-model:value="albumFormInfo.status">
              <template #checked>
                公开
              </template>
              <template #unchecked>
                私密
              </template>
            </n-switch>
          </desc-setting>
          <n-divider dashed>
            相册封面
          </n-divider>
          <n-space vertical align="center">
            <n-upload
                list-type="image-card"
                :default-file-list="albumCoverFile"
                :custom-request="uploadAlbumCoverHandel"
                @remove="removeAlbumCoverHandle"
                :max="1"
            >
              点击上传
            </n-upload>
            <span class="text-tip" v-show="albumFormInfo.albumCover.status">相册封面不可为空</span>
          </n-space>
        </n-space>
        <template #footer>
          <n-button @click="newAlbumHandle" :disabled="checkAlbumInfo">保存</n-button>
        </template>
      </n-drawer-content>
    </n-drawer>
  </n-space>
</template>

<style scoped>
.text-tip {
  @apply text-red-600 text-12px
}
</style>