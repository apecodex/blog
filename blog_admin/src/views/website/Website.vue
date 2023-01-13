<script setup lang='ts'>
import {
  websiteData,
  createWebsiteData,
  updateWebsite,
  updateWebsiteIntro,
  updateSocialLogin,
  updateCommentReview,
  updateMessageReview,
  updateEmailNotice,
  updateMessageNotice,
  updateAutoPlayer,
  updateWebsiteNotice,
  commentReviewLoading,
  messageReviewLoading,
  emailNoticeLoading,
  messageNoticeLoading,
  autoPlayerLoading,
  updateTyperText,
} from './data/useWebsite'
import {
  beforeUpload,
  websiteAvatarFile,
  websiteDefaultUserAvatarFile,
  websiteDefaultTouristAvatarFile,
  websiteBackgroundImagesFile,
  weiXinQRCodeFile,
  alipayQRCodeFile,
  updateWebsiteAvatarHandle,
  updateWebsiteDefaultUserAvatarHandle,
  updateWebsiteDefaultTouristAvatarHandle,
  websiteBackgroundImagesHandle,
  websiteBackgroundImagesRemoveHandle,
  updateWeiXinQRCodeFileHandle,
  updateAlipayQRCodeFileHandle
} from './data/useAvatar'
import {
  NSpace,
  NCard,
  NTabs,
  NTabPane,
  NText,
  NInput,
  NDynamicTags,
  NSwitch,
  NDivider,
  NSpin,
  NUpload,
} from "naive-ui";
import CommentComp from "~/components/comment/CommentComp.vue"
import {onMounted} from "vue";

onMounted(() => {
  createWebsiteData()
})

</script>

<template>
  <n-space vertical>
    <n-card>
      <n-tabs
          class="card-tabs"
          default-value="websiteInfo"
          size="large"
          animated
          style="margin: 0 -4px"
          pane-style="padding-left: 4px; padding-right: 4px; box-sizing: border-box;"
      >
        <n-tab-pane name="websiteInfo" tab="网站信息">
          <n-space vertical v-if="websiteData">
            <n-space vertical>
              <n-text type="warning">网站名称</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.websiteName" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">网站作者</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.websiteAuthor" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">邮件接收邮箱</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.receiveEmail" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">网站域名</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.url" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">网站运行时间</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.websiteCreateTime" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">网站备案</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.websiteRecordNo" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">qq</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.qq" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">微信</n-text>
              <n-input style="width: 200px" v-model:value="websiteData.wechat" @change="updateWebsite"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">网站简介</n-text>
              <comment-comp :style="{width: '350px'}" maxlength="150" show-count enter-btn-text="保存" :min-rows="3"
                            :max-rows="3"
                            v-model:default-value="websiteData.websiteIntro" @enter="updateWebsiteIntro" size="small"/>
            </n-space>
            <n-space vertical v-if="websiteData.homeTyperTexts">
              <n-text type="warning">首页打字机文本</n-text>
              <n-dynamic-tags v-model:value="websiteData.homeTyperTexts" @update:value="updateTyperText"/>
            </n-space>
            <n-space vertical>
              <n-text type="warning">第三方登录</n-text>
              <n-text v-for="(value, key) in websiteData.socialLogin" :key="key">
                <n-space align="center">
                  <n-text type="info">{{ key }}</n-text>
                  <n-switch :round="false" v-model:value="websiteData.socialLogin[key]"
                            @update:value="updateSocialLogin">
                    <template #checked>
                      开启
                    </template>
                    <template #unchecked>
                      关闭
                    </template>
                  </n-switch>
                  <n-divider vertical/>
                </n-space>
              </n-text>
            </n-space>
            <n-space align="center">
              <n-text type="warning">评论审核</n-text>
              <n-switch :round="false" :loading="commentReviewLoading" v-model:value="websiteData.isCommentReview"
                        @update:value="updateCommentReview">
                <template #checked>
                  开启
                </template>
                <template #unchecked>
                  关闭
                </template>
              </n-switch>
            </n-space>
            <n-space align="center">
              <n-text type="warning">留言审核</n-text>
              <n-switch :round="false" :loading="messageReviewLoading" v-model:value="websiteData.isMessageReview"
                        @update:value="updateMessageReview">
                <template #checked>
                  开启
                </template>
                <template #unchecked>
                  关闭
                </template>
              </n-switch>
            </n-space>
            <n-space align="center">
              <n-text type="warning">邮箱通知</n-text>
              <n-switch :round="false" :loading="emailNoticeLoading" v-model:value="websiteData.isEmailNotice"
                        @update:value="updateEmailNotice">
                <template #checked>
                  开启
                </template>
                <template #unchecked>
                  关闭
                </template>
              </n-switch>
            </n-space>
            <n-space align="center">
              <n-text type="warning">留言通知</n-text>
              <n-switch :round="false" :loading="messageNoticeLoading" v-model:value="websiteData.isMessageNotice"
                        @update:value="updateMessageNotice">
                <template #checked>
                  开启
                </template>
                <template #unchecked>
                  关闭
                </template>
              </n-switch>
            </n-space>
            <n-space align="center">
              <n-text type="warning">自动播放音乐</n-text>
              <n-switch :round="false" :loading="autoPlayerLoading" v-model:value="websiteData.isAutoPlayer"
                        @update:value="updateAutoPlayer">
                <template #checked>
                  开启
                </template>
                <template #unchecked>
                  关闭
                </template>
              </n-switch>
            </n-space>
          </n-space>
          <n-space vertical v-else>
            <n-spin></n-spin>
          </n-space>
        </n-tab-pane>
        <n-tab-pane name="websiteNotice" tab="网站公告">
          <n-space vertical v-if="websiteData">
            <comment-comp maxlength="200" show-count enter-btn-text="保存" :min-rows="4"
                          :max-rows="4"
                          v-model:default-value="websiteData.websiteNotice" @enter="updateWebsiteNotice" size="small"/>
          </n-space>
        </n-tab-pane>
        <n-tab-pane name="websiteBackgroundImages" tab="首页背景图">
          <n-space vertical v-if="websiteData">
            <n-upload
                :default-file-list="websiteBackgroundImagesFile"
                list-type="image-card"
                :max="6"
                multiple
                @before-upload="beforeUpload"
                @remove="websiteBackgroundImagesRemoveHandle"
                :custom-request="websiteBackgroundImagesHandle"
            >
              点击上传
            </n-upload>
          </n-space>
        </n-tab-pane>
        <n-tab-pane name="websiteAvatar" tab="网站头像">
          <n-space align="center">
            <n-space vertical>
              <n-text type="warning">网站头像</n-text>
              <n-upload
                  :default-file-list="websiteAvatarFile"
                  list-type="image-card"
                  :max="1"
                  @before-upload="beforeUpload"
                  :custom-request="updateWebsiteAvatarHandle"
              >
                点击上传
              </n-upload>
            </n-space>
            <n-space vertical>
              <n-text type="warning">默认用户头像</n-text>
              <n-upload
                  :default-file-list="websiteDefaultUserAvatarFile"
                  list-type="image-card"
                  :max="1"
                  @before-upload="beforeUpload"
                  :custom-request="updateWebsiteDefaultUserAvatarHandle"
              >
                点击上传
              </n-upload>
            </n-space>
            <n-space vertical>
              <n-text type="warning">默认游客头像</n-text>
              <n-upload
                  :default-file-list="websiteDefaultTouristAvatarFile"
                  list-type="image-card"
                  :max="1"
                  @before-upload="beforeUpload"
                  :custom-request="updateWebsiteDefaultTouristAvatarHandle"
              >
                点击上传
              </n-upload>
            </n-space>
          </n-space>
        </n-tab-pane>
        <n-tab-pane name="websiteQRCode" tab="打赏码">
          <n-space align="center">
            <n-space vertical>
              <n-text type="warning">微信打赏码</n-text>
              <n-upload
                  :default-file-list="weiXinQRCodeFile"
                  list-type="image-card"
                  :max="1"
                  @before-upload="beforeUpload"
                  :custom-request="updateWeiXinQRCodeFileHandle"
              >
                点击上传
              </n-upload>
            </n-space>
            <n-space vertical>
              <n-text type="warning">支付宝打赏码</n-text>
              <n-upload
                  :default-file-list="alipayQRCodeFile"
                  list-type="image-card"
                  :max="1"
                  @before-upload="beforeUpload"
                  :custom-request="updateAlipayQRCodeFileHandle"
              >
                点击上传
              </n-upload>
            </n-space>
          </n-space>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </n-space>
</template>

<style scoped>
</style>