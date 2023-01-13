import {computed, reactive, ref} from "vue"
import {sendEmailCode, updateUserPassword} from "@/api/requests/User";
import {notify} from "@kyvg/vue3-notification";
import {storeToRefs} from "pinia";
import {useSettingStore, useUserInfoStore} from "@/store";
import {StatusCode} from "@/api/enum/StatusCode";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const {countDown, loadingFlag} = storeToRefs(settingStore);
const {userInfo} = storeToRefs(userInfoStore);

const passwordForm = reactive({
    captchaVerification: "",
    code: "",
    newPassword: "",
    oldPassword: ""
})

const showOldPasswordStatus = ref(false);
const showNewPasswordStatus = ref(false);

const showOldPassword = () => {
    showOldPasswordStatus.value = !showOldPasswordStatus.value
}
const showNewPassword = () => {
    showNewPasswordStatus.value = !showNewPasswordStatus.value
}

const checkPasswordFormComp = computed(() => {
    const code = passwordForm.code.trim().length === 0;
    const oldPassword = passwordForm.oldPassword.length === 0;
    const newPassword = passwordForm.newPassword.length === 0;
    return code || oldPassword || newPassword;
})

const sendEmailHandle = async () => {
    if (!userInfoStore.isLogin) {
        return;
    }
    if ((passwordForm.oldPassword.length === 0) || (passwordForm.newPassword.length === 0)) {
        notify({
            text: "请先输入旧密码和新密码",
            type: "warn"
        });
        return;
    }
    if (!countDown.value.countDownIng && !countDown.value.timer) {
        countDown.value.countDownIng = true;
        settingStore.countDownHandle();
        await sendEmailCode(userInfo.value!.email).then((resp: ResultObject<null>) => {
            if (resp.status) {
                notify({
                    text: `验证码已发送至 ${userInfo.value!.email} 请注意查收~`,
                })
            } else {
                if (resp.code === StatusCode.FAIL) {
                    notify({
                        text: resp.message,
                        type: "warn"
                    })
                }
            }
        }).catch(() => {
            // 发送失败，倒计时清空
            countDown.value.countDownTime = 60;
            countDown.value.countDownIng = false;
            countDown.value.startTime = null;
            if (countDown.value.timer) {
                clearInterval(countDown.value.timer);
                countDown.value.timer = null;
            }
            notify({
                text: `验证码发送失败，请重试`,
            });
        });
    }
}

const verifyHandle =  async (verify: { captchaVerification: string }) => {
    passwordForm.captchaVerification = verify.captchaVerification;
    notify({
        text: "正在保存...",
    });
    loadingFlag.value = true;
    const form = new FormData();
    form.append("captchaVerification", passwordForm.captchaVerification);
    form.append("code", passwordForm.code);
    form.append("newPassword", passwordForm.newPassword);
    form.append("oldPassword", passwordForm.oldPassword);
    await updateUserPassword(form).then((resp: ResultObject<null>) => {
        if (resp.status) {
            userInfoStore.logout();
            notify({
                text: "修改成功，请重新登录",
                type: "success"
            });
        } else {
            if (resp.code === StatusCode.FAIL) {
                notify({
                    text: resp.message,
                    type: "warn"
                });
            }
        }
    }).catch(() => {
        notify({
            text: "修改失败，请重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
    })
}

export {
    passwordForm,
    showOldPasswordStatus,
    showNewPasswordStatus,
    showOldPassword,
    showNewPassword,
    sendEmailHandle,
    verifyHandle,
    checkPasswordFormComp
}