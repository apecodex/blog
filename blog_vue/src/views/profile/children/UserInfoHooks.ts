import type {Ref} from "vue";
import {getUserInfo, updateUserInfo} from "@/api/requests/User"
import {useSettingStore, useUserInfoStore, useWebsiteInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {reactive, ref} from "vue";
import {StatusCode} from "@/api/enum/StatusCode";
import {router} from "@/router"

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const {loadingFlag, loginFlag, bindOrUnbindEmailFlag} = storeToRefs(settingStore);
const {userInfo} = storeToRefs(userInfoStore);

const userInfoData: Ref<UserInfoModel | null> = ref(null);
const userInfoForm = reactive({
    intro: "",
    isEmailNotice: false,
    nickname: "",
    webSite: ""
})

const loginType = {
    0: '邮箱',
    1: "QQ"
}

const createUserInfoData = async () => {
    if (!userInfoStore.isLogin) {
        settingStore.closeModal();
        loginFlag.value = true;
        notify({
            text: "请先登录",
            type: "warn"
        });
        return;
    }
    loadingFlag.value = true;
    userInfoData.value = null;
    await getUserInfo().then((resp: ResultObject<UserInfoModel>) => {
        if (resp.status) {
            userInfoData.value = resp.data;
            userInfoForm.intro = resp.data.intro;
            userInfoForm.isEmailNotice = resp.data.isEmailNotice;
            userInfoForm.nickname = resp.data.nickname;
            userInfoForm.webSite = resp.data.webSite;
        } else {
            if (resp.code === StatusCode.UNAUTHORIZED) {
                router.replace({path: "/"});
            }
            notify({
                text: resp.message,
                type: "warn"
            })
        }
    }).catch(() => {
        notify({
            text: "用户信息获取失败，请重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
    })
}

const saveIntroHandle = async ({commentText}: any) => {
    userInfoForm.intro = commentText.value;
    await updateUserInfoHandle();
}

const updateUserInfoHandle = async () => {
    if (userInfoForm.nickname.length === 0) {
        notify({
            text: "用户昵称不能为空",
            type: "warn"
        })
        return;
    }
    if (userInfoData.value?.isEmailNotice !== userInfoForm.isEmailNotice) {
        if (!userInfoData.value?.email) {
            notify({
                text: "请先绑定邮箱",
                type: "warn"
            });
            userInfoForm.isEmailNotice = false;
            return;
        }
    }
    notify({
        text: "正在保存...",
    });
    loadingFlag.value = true;
    await updateUserInfo(userInfoForm).then((resp: ResultObject<null>) => {
        if (resp.status) {
            userInfo.value!.nickname = userInfoForm.nickname
            notify({
                text: resp.message,
                type: "success"
            });
        } else {
            if (resp.code === StatusCode.FAIL) {
                notify({
                    text: resp.message,
                    type: "warn"
                })
            }
        }
    }).catch(() => {
        notify({
            text: "保存失败，请重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
    })
}

const openBindEmailModal = () => {
    settingStore.closeModal();
    bindOrUnbindEmailFlag.value = true;
}

export {
    userInfoData,
    userInfoForm,
    createUserInfoData,
    loginType,
    saveIntroHandle,
    updateUserInfoHandle,
    openBindEmailModal,
}