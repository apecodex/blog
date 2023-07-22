import Names from "./store-name"
import type { Ref } from "vue"
import { defineStore } from "pinia";
import { ref } from "vue";

export const useWebsiteInfoStore = defineStore(Names.WEBSITE, () => {
  const websiteInfo: Ref<WebsiteInfoEntity | null> = ref<WebsiteInfoEntity | null>(null);
  // 用户经纬度
  const rectangle = ref("");

  // 第三方登录前的url地址
  const originalLoginUrl = ref("");

  // 在线人数
  const onlineCount: Ref<number> = ref(0);

  // 更改在线人数
  const changeOnlineCount = (count: number) => {
    onlineCount.value = count;
  }

  // 保持网站信息
  const saveWebsiteInfo = (info: WebsiteInfoEntity) => {
    websiteInfo.value = info
  }

  return {
    websiteInfo,
    saveWebsiteInfo,
    rectangle,
    originalLoginUrl,
    onlineCount,
    changeOnlineCount
  }
}, { persist: { storage: sessionStorage } })