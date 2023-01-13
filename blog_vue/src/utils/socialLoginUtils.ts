import {useWebsiteInfoStore} from "@/store";
import {storeToRefs} from "pinia";
import {router} from "@/router";
import {isMobile} from "@/utils/utils";
import config from "@/config";

let websiteInfoStore = null;

// QQ登录
export const qqLogin = () => {
  // 这是坑，不放在这里，会出错，因为在外部使用时pinia还没有加载
  websiteInfoStore = useWebsiteInfoStore();
  const {originalLoginUrl} = storeToRefs(websiteInfoStore);
  originalLoginUrl.value = router.currentRoute.value.path;
  // 移动端
  if (isMobile()) {
    QC.Login.showPopup({
      appId: config.QQ_APPID,
      redirectURI: config.QQ_REDIRECT_URI
    });
  } else {
    const socialLoginUrl = `https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=${config.QQ_APPID}&redirect_uri=${config.QQ_REDIRECT_URI}&state=1000&scope=all&display=pc`;
    window.open(socialLoginUrl, "_self")
  }
}

// 是否展示登录图标
export const showLoginIcon = (type: string) => {
  websiteInfoStore = useWebsiteInfoStore();
  const {websiteInfo} = storeToRefs(websiteInfoStore);
  if (websiteInfo.value && websiteInfo.value.socialLogin.hasOwnProperty(type)) {
    return (<any>websiteInfo.value!.socialLogin)[type];
  }
  return false;
}