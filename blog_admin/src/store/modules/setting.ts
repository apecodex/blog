import type { SettingConfig, TriggerStyle } from '~/settings';
import { Names } from "./store-name"
import settings from "~/settings"
import {defineStore} from "pinia";
import {computed, reactive, toRefs} from "vue";

export const useSettingStore = defineStore(Names.SETTING, () => {
  const setting = reactive(settings)
  const changeSetting = (
    key: keyof SettingConfig,
    value: boolean | TriggerStyle
  ) => {
    // @ts-ignore
    setting[key] = value
  }

  //
  const editorTheme = computed(() => {
    return setting.globalTheme === "darkTheme" ? "dark" : "";
  })

  return {
    ...toRefs(setting),
    changeSetting,
    editorTheme
  }
}, { persist: { storage: localStorage } })