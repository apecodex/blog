import { App } from 'vue'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { createPinia } from "pinia";

export { useGlobalStore } from "./modules/global"
export { useSettingStore } from './modules/setting'
export { useWebsiteInfoStore } from './modules/websiteInfo'
export { useUserInfoStore } from './modules/userInfo'
export { useWebSocketStore } from "./modules/websocket"

const store = createPinia();
store.use(piniaPluginPersistedstate)

export function setupStore(app: App<Element>) {
  app.use(store);
}