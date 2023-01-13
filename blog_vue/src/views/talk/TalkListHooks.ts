import type {Ref} from "vue";
import {getTalk} from "@/api/requests/Talk"
import {useSettingStore, useUserInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {reactive, ref} from "vue";
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const {loadingFlag} = storeToRefs(settingStore);
const {userInfo} = storeToRefs(userInfoStore);

const talksData: Ref<{ count: number, recordList: Array<TalkEntity> } | null> = ref(null)
const pagination = reactive({
    size: 10,
    current: 1
} as ConditionParams)

// 处理说说数据
const talksDataHandle = (talks: Array<TalkEntity>) => {
    talks.map((talk: TalkEntity) => {
        if (!talk.likeCount) {
            talk.likeCount = 0;
        } else {
            if (userInfo.value?.talkLikeSet.indexOf(talk.id) !== -1) {
                talk.isLike = true;
            } else talk.isLike = false;
        }
        if (!talk.commentCount) {
            talk.commentCount = 0;
        }
        if (talk.src.length >= 4) {
            talk.src = talk.src.splice(0, 4);
        }
        talk.createTime = new Date(talk.createTime);
    })
}

const createTalksData = async ({size, current}: ConditionParams) => {
    loadingFlag.value = true;
    talksData.value = null;
    await getTalk({size, current}).then((resp: PageResult<Array<TalkEntity>>) => {
        if (resp.status) {
            talksDataHandle(resp.data.recordList)
            talksData.value = resp.data
        }
    }).catch(() => {
        notify({
            text: "获取说说列表失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false
        scrollDown();
    })
}

const updateTalksData = async ({size, current}: ConditionParams) => {
    loadingFlag.value = true;
    await getTalk({size, current}).then((resp: PageResult<Array<TalkEntity>>) => {
        if (resp.status) {
            talksDataHandle(resp.data.recordList)
            talksData.value?.recordList.push(...resp.data.recordList);
        }
    }).catch(() => {
        notify({
            text: "获取说说列表失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false
    })
}

const moreTalkHandle = async () => {
    if (talksData.value?.recordList.length !== talksData.value?.count) {
        pagination.current!++;
        await updateTalksData(pagination)
    }
}
export {
    pagination,
    talksData,
    createTalksData,
    updateTalksData,
    moreTalkHandle
}