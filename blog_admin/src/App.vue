<script setup lang="ts">
import GlobalProvider from "~/components/GlobalProvider.vue"
import { darkTheme, zhCN, dateZhCN, NConfigProvider } from 'naive-ui';
import { router } from './router';
import { useSettingStore, useUserInfoStore, useMenuStore } from './store/';
import {storeToRefs} from "pinia";
import {computed, onBeforeMount} from "vue";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const menuStore = useMenuStore();
const { globalTheme } = storeToRefs(settingStore);

const theme = computed(() => {
  if (globalTheme.value === "darkTheme") {
    return darkTheme
  } else {
    return null;
  }
})

onBeforeMount(() => {
  const interceptPage = () => {
    // 未登录用户跳转至登录页
    if (userInfoStore.userInfo === null || menuStore.menus === null) {
      userInfoStore.removeUserInfo()
      menuStore.clearMenu()
      router.replace({ path: "/login" });
    }
  };
  interceptPage();
})

</script>

<template>
  <n-config-provider :theme="theme" :locale="zhCN" :dateLocale="dateZhCN">
    <global-provider>
      <RouterView />
    </global-provider>
  </n-config-provider>
</template>

<style scoped>
</style>
