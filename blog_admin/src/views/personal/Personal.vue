<script setup lang='ts'>
import {
  personalAvatarFile,
  beforeUpload,
  userInfoData,
  getUserInfoData,
  updateUserAvatarHandle,
  updateUserInfoHandle,
  updateIntro,
} from './data/usePersonal'
import {usePassword} from "./data/usePassword"
import {
  NSpace,
  NCard,
  NTabs,
  NTabPane,
  NText,
  NUpload,
  NButton,
  NTag,
  NInputGroup,
  NInput,
  NSwitch,
  NForm,
  NFormItem,
  NPopover,
  NCountdown
} from "naive-ui";
import CommentComp from "~/components/comment/CommentComp.vue"
import {onBeforeRouteLeave} from "vue-router";
import {onMounted} from "vue";
import Verify from "~/components/verifition/Verify.vue";

const {
  passwordData,
  verify,
  formRef,
  rules,
  startCount,
  loginType,
  renderCountdown,
  handlePasswordInput,
  sendEmailCodeHandle,
  showCaptcha,
  handleValidate
} = usePassword();

onMounted(() => {
  getUserInfoData()
})

// 离开前清空表单
onBeforeRouteLeave(() => {
  passwordData.value.code = ""
  passwordData.value.oldPassword = ""
  passwordData.value.newPassword = ""
  passwordData.value.reenteredPassword = ""
})

</script>

<template>
  <n-space vertical v-if="userInfoData">
    <n-card>
      <n-tabs
        class="card-tabs"
        default-value="personal"
        size="large"
        animated
        style="margin: 0 -4px"
        pane-style="padding-left: 4px; padding-right: 4px; box-sizing: border-box;"
      >
        <n-tab-pane name="personal" tab="用户信息">
          <n-card>
            <n-space vertical>
              <n-space vertical>
                <n-text type="warning">用户头像</n-text>
                <n-upload
                  :default-file-list="personalAvatarFile"
                  list-type="image-card"
                  :max="1"
                  @before-upload="beforeUpload"
                  :custom-request="updateUserAvatarHandle"
                >
                  点击上传
                </n-upload>
              </n-space>
              <n-space align="center">
                <n-text type="warning">uid</n-text>
                <n-tag type="error">{{ userInfoData.uid }}</n-tag>
              </n-space>
              <n-space align="center">
                <n-text type="warning">登录方式</n-text>
                <n-tag type="info">{{ loginType }}</n-tag>
              </n-space>
              <n-input-group>
                <n-space align="center">
                  <n-text type="warning">邮箱</n-text>
                  <n-tag strong>{{ userInfoData.email }}</n-tag>
                </n-space>
              </n-input-group>
              <n-input-group>
                <n-space vertical>
                  <n-text type="warning">用户名</n-text>
                  <n-input v-model:value="userInfoData.nickname" @change="updateUserInfoHandle" style="width: 200px"/>
                </n-space>
              </n-input-group>
              <n-input-group>
                <n-space vertical>
                  <n-text type="warning">个人网站</n-text>
                  <n-input v-model:value="userInfoData.webSite" @change="updateUserInfoHandle" style="width: 200px"/>
                </n-space>
              </n-input-group>
              <n-space align="center">
                <n-text type="warning">开启邮箱通知</n-text>
                <n-switch :round="false" v-model:value="userInfoData.isEmailNotice"
                          @update:value="updateUserInfoHandle">
                  <template #checked>
                    是
                  </template>
                  <template #unchecked>
                    否
                  </template>
                </n-switch>
              </n-space>
              <CommentComp maxlength="150" show-count v-model:default-value="userInfoData.intro" @enter="updateIntro"
                           enter-btn-text="保存" :min-rows="2" :max-rows="4"/>
            </n-space>
          </n-card>
        </n-tab-pane>
        <n-tab-pane name="updatePassword" tab="修改密码">
          <n-card>
            <n-form ref="formRef" :model="passwordData" :rules="rules">
              <n-form-item path="oldPassword" label="旧密码">
                <n-input type="password" style="width: 200px" v-model:value="passwordData.oldPassword"
                         @keydown.enter.prevent/>
              </n-form-item>
              <n-form-item path="newPassword" label="新密码">
                <n-input
                  v-model:value="passwordData.newPassword"
                  type="password"
                  style="width: 200px"
                  @input="handlePasswordInput"
                  @keydown.enter.prevent
                />
              </n-form-item>
              <n-form-item
                ref="rPasswordFormItemRef"
                first
                path="reenteredPassword"
                label="重复密码"
              >
                <n-input
                  v-model:value="passwordData.reenteredPassword"
                  :disabled="!passwordData.newPassword"
                  type="password"
                  style="width: 200px"
                  @keydown.enter.prevent
                />
              </n-form-item>
              <n-form-item label="验证码" path="code">
                <n-input v-model:value="passwordData.code" style="width: 200px" placeholder="输入验证码"/>
                <n-popover trigger="hover">
                  <template #trigger>
                    <n-button type="primary" ghost @click="sendEmailCodeHandle" :disabled="startCount">
                      <n-countdown v-if="startCount" @finish="startCount = false" :render="renderCountdown"
                                   :duration="60000" :active="true"/>
                      <n-text v-else>发送验证码</n-text>
                    </n-button>
                  </template>
                  <span>{{ userInfoData.email }}</span>
                </n-popover>
              </n-form-item>
              <n-form-item>
                <n-button size="small" tertiary @click.prevent="showCaptcha">
                  提交
                </n-button>
              </n-form-item>
            </n-form>
          </n-card>
        </n-tab-pane>
      </n-tabs>
    </n-card>
    <Verify @success="handleValidate" mode="pop" captchaType="blockPuzzle" ref="verify"/>
  </n-space>
</template>

<style scoped>
</style>