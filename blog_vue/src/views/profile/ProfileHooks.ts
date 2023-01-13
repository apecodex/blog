import {notify} from "@kyvg/vue3-notification"
import {updateUserAvatar} from "@/api/requests/User"
import {useSettingStore, useUserInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {StatusCode} from "@/api/enum/StatusCode";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const {loginFlag, loadingFlag} = storeToRefs(settingStore);
const {userInfo} = storeToRefs(userInfoStore);

let fileTypes = ["image/png", "image/gif", "image/jpeg", "image/jpg"];

const updateUserAvatarHandle = async (event: Event) => {
    if (!userInfoStore.isLogin) {
        settingStore.closeModal();
        loginFlag.value = true;
    }
    const target = event.target || event.srcElement;
    let files = (<any>target).files as FileList
    // 取消上传
    if (files.length === 0) {
        return;
    }
    if (fileTypes.indexOf(files[0].type) === -1) {
        notify({
            text: "不接受此文件类型！",
            type: "warn"
        })
        return;
    }
    if (files[0].size > (1024 * 1024 * 3)) {
        notify({
            text: "图片大小要求在3MB以内~",
            type: "warn"
        })
        return;
    }
    const form = new FormData()
    form.append("file", files[0]);
    notify({
        text: "正在上传..."
    })
    loadingFlag.value = true;
    await updateUserAvatar(form).then((resp: ResultObject<UploadFileInfoModel>) => {
        if (resp.status) {
            if (userInfo.value) {
                userInfo.value.avatar = resp.data.url
            }
            notify({
                text: resp.message,
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
            text: "上传失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
        (<any>target).value = "";
    })
}

export {
    updateUserAvatarHandle
}