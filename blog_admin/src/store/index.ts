import { App } from 'vue';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import {createPinia} from "pinia";
export { useGlobalStore } from './modules/global'
export { useWebsiteStore } from './modules/website'
export { useMenuStore } from './modules/menu';
export { useSettingStore } from './modules/setting'
export { useUserInfoStore } from './modules/userInfo'
export { useTagsViewStore } from './modules/tagsView'

const store = createPinia();
store.use(piniaPluginPersistedstate)
export function setupStore(app: App<Element>) {
  app.use(store);
}