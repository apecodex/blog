import type {Ref} from "vue";
import {friendLinkForm, clearFriendLinkForm, checkFriendLinkForm, lockBtn} from "@/components/modal/FriendLinkHooks"
import {getUserFriendLink, saveOrUpdateFriendLink} from "@/api/requests/FriendLink"
import {ref} from "vue";
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {StatusCode} from "@/api/enum/StatusCode";

const settingStore = useSettingStore();
const {loadingFlag, friendLinkFlag} = storeToRefs(settingStore);

const userFriendLinkData: Ref<Array<UserFriendLinkEntity> | null> = ref(null)
const createUserFriendLink = async () => {
    loadingFlag.value = true;
    userFriendLinkData.value = null;
    await getUserFriendLink().then((resp: ResultObject<Array<UserFriendLinkEntity>>) => {
        if (resp.status) {
            if (resp.data.length !== 0) {
                friendLinkForm.id = resp.data[0].id;
                friendLinkForm.linkName = resp.data[0].linkName;
                friendLinkForm.linkAvatar = resp.data[0].linkAvatar;
                friendLinkForm.linkUrl = resp.data[0].linkUrl;
                friendLinkForm.linkIntro = resp.data[0].linkIntro;
            }
            userFriendLinkData.value = resp.data;
        }
    }).catch(() => {
        notify({
            text: "友链获取失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
    })
}

const updateFriendLinkHandle = async () => {
    if (checkFriendLinkForm) {
        const form = new FormData()
        form.append("id", friendLinkForm.id as string);
        form.append("linkAvatar", friendLinkForm.linkAvatar as string);
        form.append("linkIntro", friendLinkForm.linkIntro as string);
        form.append("linkName", friendLinkForm.linkName as string);
        form.append("linkUrl", friendLinkForm.linkUrl as string);
        lockBtn.value = true;
        notify({
            text: "正在提交申请...",
        });
        loadingFlag.value = true;
        await saveOrUpdateFriendLink(form).then((resp: ResultObject<null>) => {
            if (resp.status) {
                notify({
                    text: "申请成功，审核结果会尽快返回给你，请留意哦~",
                    type: "success"
                });
                createUserFriendLink();
            } else {
                if (resp.code === StatusCode.FAIL) {
                    notify({
                        text: resp.message,
                        type: "warn"
                    });
                    clearFriendLinkForm();
                }
            }
        }).catch(() => {
            notify({
                text: "提交失败，请重试",
                type: "warn"
            })
        }).finally(() => {
            lockBtn.value = false;
            loadingFlag.value = false;
        })
    }
}

const openFriendLinkModal = () => {
    clearFriendLinkForm();
    settingStore.closeModal();
    friendLinkFlag.value = true;
}
export {
    userFriendLinkData,
    createUserFriendLink,
    updateFriendLinkHandle,
    openFriendLinkModal
}