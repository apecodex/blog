import Names from './store-name'
import type {Ref} from "vue"
import {defineStore} from "pinia";
import {computed, reactive, toRefs, ref} from "vue";
import settings from '@/settings'

export const useSettingStore = defineStore(Names.SETTING, () => {
    const setting = reactive(settings)

    const closeModal = () => {
        setting.loginFlag = false;
        setting.registerFlag = false;
        setting.searchFlag = false;
        setting.friendLinkFlag = false;
        setting.forgetPasswordFlag = false;
        setting.bindOrUnbindEmailFlag = false;
        document.body.style.overflow = 'visible';
    }

    const theme = computed(() => {
        return setting.globalTheme === "darkTheme" ? "dark" : "";
    })

    // 倒计时
    const countDown: Ref<{ countDownTime: number, timer: NodeJS.Timer | null, countDownIng: boolean, startTime: null | number }> = ref({
        countDownTime: 60,
        timer: null,
        countDownIng: false,
        startTime: null
    })

    // 开始倒计时
    const countDownHandle = () => {
        let nowTime = new Date().getTime();
        if (countDown.value.startTime) {
            let surplus = 60 - parseInt(String((nowTime - countDown.value.startTime) / 1000), 10);
            countDown.value.countDownTime = surplus <= 0 ? 0 : surplus;
        } else {
            countDown.value.countDownTime = 60;
            countDown.value.startTime = nowTime;
        }
        countDown.value.timer = setInterval(() => {
            countDown.value.countDownTime--;
            countDown.value.countDownIng = true;
            if (countDown.value.countDownTime <= 0) {
                countDown.value.startTime = null;
                clearInterval(countDown.value.timer as NodeJS.Timer);
                countDown.value.countDownTime = 60;
                countDown.value.countDownIng = false;
                countDown.value.timer = null;
            }
        }, 1000);
    }

    return {
        ...toRefs(setting),
        closeModal,
        theme,
        countDown,
        countDownHandle
    }

}, {persist: {storage: localStorage}})