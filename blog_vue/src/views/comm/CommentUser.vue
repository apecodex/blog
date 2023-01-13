<script setup lang='ts'>
import BoxComponent from "@/components/BoxComponent.vue"
import {useWebsiteInfoStore, useUserInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {computed} from "vue";

const websiteInfoStore = useWebsiteInfoStore();
const userInfoStore = useUserInfoStore();
const {websiteInfo} = storeToRefs(websiteInfoStore);
const {userInfo} = storeToRefs(userInfoStore);

const avatarComp = computed(() => {
  if (userInfoStore.isLogin) return userInfo.value?.avatar;
  else return websiteInfo.value?.touristAvatar;
})
</script>

<template>
  <BoxComponent class="p-10px">
    <div class="comment-wrapper flex gap-10px">
      <img class="w-60px h-60px rounded-6px <md:(w-40px h-40px)" :src="avatarComp" alt="">
      <slot/>
    </div>
  </BoxComponent>
</template>

<style scoped>

</style>