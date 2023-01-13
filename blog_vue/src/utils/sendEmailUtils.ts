import {matchEmail} from "@/utils/utils";
import {sendEmailCode} from "@/api/requests/User";
import {notify} from "@kyvg/vue3-notification";
import {StatusCode} from "@/api/enum/StatusCode";
import {useSettingStore} from "@/store";
import {storeToRefs} from "pinia";

let settingStore = null;
export const sendEmail = async (email: string) => {
  settingStore = useSettingStore();
  const {countDown} = storeToRefs(settingStore)
  if (!countDown.value.countDownIng && matchEmail(email)) {
    countDown.value.countDownIng = true;
    settingStore.countDownHandle();
    await sendEmailCode(email).then((resp: ResultObject<null>) => {
      if (resp.status) {
        notify({
          text: `验证码已发送至 ${email} 请注意查收~`,
        })
      }
      if (resp.code === StatusCode.ACCESS_LIMIT) {
        clearCountDown(countDown);
      }
    }).catch(() => {
      // 发送失败，倒计时清空
      clearCountDown(countDown);
      notify({
        text: `验证码发送失败，请重试`,
      });
    });
  }
}

const clearCountDown = (countDown: any) => {
  // 发送失败，倒计时清空
  countDown.value.countDownTime = 60;
  countDown.value.countDownIng = false;
  countDown.value.startTime = null;
  if (countDown.value.timer) {
    clearInterval(countDown.value.timer);
    countDown.value.timer = null;
  }
}