import type {Ref} from "vue"
import {ref} from "vue"
import {getFriendLink} from "@/api/requests/FriendLink"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {scrollDown} from "@/utils/utils";
import {clearFriendLinkForm} from "@/components/modal/FriendLinkHooks";

const settingStore = useSettingStore()
const {loadingFlag} = storeToRefs(settingStore);

const friendLinksData: Ref<{count: number, recordList: Array<FriendLinkEntity>} | null> = ref(null);

// 获取友链列表
const createFriendLinksData = async ({size, current}: ConditionParams) => {
    loadingFlag.value = true;
    // 清空友链表单
    clearFriendLinkForm();
    await getFriendLink({size,current}).then((resp: PageResult<Array<FriendLinkEntity>>) => {
        if (resp.status) {
            friendLinksData.value = resp.data
        }
    }).catch(() => {
        notify({
            text: "友链获取失败，请重试",
            type: "warm"
        });
    }).finally(() => {
        loadingFlag.value = false;
        scrollDown();
    })
}

export {
    friendLinksData,
    createFriendLinksData
}