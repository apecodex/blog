import Names from "./store-name"
import type {Ref} from "vue"
import {defineStore} from "pinia";
import {ref} from "vue";

export const useWebsiteInfoStore = defineStore(Names.WEBSITE, () => {
    const websiteInfo: Ref<WebsiteInfoEntity | null> = ref<WebsiteInfoEntity | null>(null);
    const rectangle = ref("");
    // 第三方登录前的url地址
    const originalLoginUrl = ref("");

    // 保持网站信息
    const saveWebsiteInfo = (info: WebsiteInfoEntity) => {
        websiteInfo.value = info
    }

    return {
        websiteInfo,
        saveWebsiteInfo,
        rectangle,
        originalLoginUrl
    }
}, {persist: {storage: sessionStorage}})