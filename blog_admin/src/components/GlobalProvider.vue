<script setup lang='ts'>
import type {ConfigProviderProps} from 'naive-ui'
import {
  darkTheme,
  lightTheme,
  NDialogProvider,
  NNotificationProvider,
  NMessageProvider,
  NLoadingBarProvider
} from 'naive-ui';
import {createDiscreteApi} from 'naive-ui'
import {useSettingStore,} from '~/store/';
import {storeToRefs} from "pinia";
import {computed} from "vue";

const settingStore = useSettingStore();
const {globalTheme} = storeToRefs(settingStore);


const themeCom = computed(() => {
  if (globalTheme.value === "darkTheme") {
    return darkTheme
  } else {
    return lightTheme;
  }
})


const configProviderPropsRef = computed<ConfigProviderProps>(() => ({
  theme: themeCom.value
}))

const {message, notification, dialog, loadingBar} = createDiscreteApi(
    ['message', 'dialog', 'notification', 'loadingBar'],
    {
      configProviderProps: configProviderPropsRef
    }
)
const win: any = window;
win.$dialog = dialog
win.$message = message
win.$loadingBar = loadingBar
win.$notification = notification

</script>

<template>
  <n-dialog-provider>
    <n-notification-provider>
      <n-message-provider>
        <n-loading-bar-provider>
          <slot/>
        </n-loading-bar-provider>
      </n-message-provider>
    </n-notification-provider>
  </n-dialog-provider>
</template>

<style scoped>
</style>