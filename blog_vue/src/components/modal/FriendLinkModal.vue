<script setup lang='ts'>
import Modal from "@/components/modal/Modal.vue";
import {useSettingStore, useUserInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {saveOrUpdateFriendLink} from "@/api/requests/FriendLink";
import {notify} from "@kyvg/vue3-notification";
import {
  lockBtn,
  friendLinkForm,
  clearFriendLinkForm,
  checkFriendLinkForm
} from "./FriendLinkHooks"
import {StatusCode} from "@/api/enum/StatusCode";

const settingStore = useSettingStore();
const {friendLinkFlag} = storeToRefs(settingStore);

const userInfoStore = useUserInfoStore();
const {loadingFlag, loginFlag} = storeToRefs(settingStore);

const closeModal = () => {
  settingStore.closeModal();
  clearFriendLinkForm();
}

const saveOrUpdateFriendLinkHandle = async (event: SubmitEvent) => {
  event.preventDefault();
  if (!userInfoStore.isLogin) {
    notify({
      text: "请先登录",
      type: "warn"
    });
    closeModal();
    loginFlag.value = true;
    return;
  }
  if (checkFriendLinkForm) {
    const form = new FormData()
    form.append("id", friendLinkForm.id as string);
    form.append("linkAvatar", friendLinkForm.linkAvatar as string);
    form.append("linkIntro", friendLinkForm.linkIntro as string);
    form.append("linkName", friendLinkForm.linkName as string);
    form.append("linkUrl", friendLinkForm.linkUrl as string);
    lockBtn.value = true;
    notify({
      text: "正在提交申请...",
    });
    loadingFlag.value = true;
    await saveOrUpdateFriendLink(form).then((resp: ResultObject<null>) => {
      if (resp.status) {
        notify({
          text: "申请成功，审核结果会尽快返回给你，请留意哦~",
          type: "success"
        });
        closeModal();
      } else {
        if (resp.code === StatusCode.FAIL) {
          notify({
            text: resp.message,
            type: "warn"
          });
          clearFriendLinkForm();
        }
      }
    }).catch(() => {
      notify({
        text: "提交失败，请重试",
        type: "warn"
      })
    }).finally(() => {
      lockBtn.value = false;
      loadingFlag.value = false;
    })
  }
}
</script>

<template>
  <Modal title="申请友链" v-model:show="friendLinkFlag" @closeModal="closeModal">
    <p class="text-center">感谢支持！提交后会尽快审核的~</p>
    <hr class="hr-twill p-1px">
    <form class="flex flex-col px-2px gap-5px" @submit="saveOrUpdateFriendLinkHandle">
      <div class="flex flex-col gap-8px">
        <label>网站名称</label>
        <input type="text" v-model="friendLinkForm.linkName" class="friend-link-input shadow" placeholder="网站叫啥？">
      </div>
      <div class="flex flex-col gap-8px">
        <label>网站头像</label>
        <input type="text" v-model="friendLinkForm.linkAvatar" class="friend-link-input shadow" placeholder="图片url地址">
      </div>
      <div class="flex flex-col gap-8px">
        <label>网站域名</label>
        <input type="text" v-model="friendLinkForm.linkUrl" class="friend-link-input shadow" placeholder="域名或IP">
      </div>
      <div class="flex flex-col gap-8px">
        <label>网站介绍 <span class="text-12px text-red-400">(可选)</span></label>
        <input type="text" v-model="friendLinkForm.linkIntro" class="friend-link-input shadow" placeholder="说点啥吧？~">
      </div>
      <div class="submit-btn mt-10px flex justify-center items-center">
        <button type="submit" class="py-4px px-15px mt-5px text-16px rounded-6px shadow"
                :class="{'!cursor-not-allowed': checkFriendLinkForm}" :disabled="checkFriendLinkForm">申 请
        </button>
      </div>
    </form>
  </Modal>
</template>

<style scoped>
.friend-link-input {
  @apply 'p-5px rounded-6px outline-none bg-$theme-bg text-16px';
}

.friend-link-input::placeholder {
  @apply 'text-$hover-color2 text-14px';
}

.friend-link-input:focus {
  box-shadow: var(--theme-shadow-inset) !important;
  animation: justshake 0.3s forwards;
}

.submit-btn > button {
  transition: var(--theme-transition), var(--theme-transition-shadow);
}

.submit-btn > button:hover {
  @apply 'bg-$theme-bg-reverse text-$text-color-reverse';
}

</style>