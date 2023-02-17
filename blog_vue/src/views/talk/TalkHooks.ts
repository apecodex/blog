import type {Ref} from "vue";
import {getTalkByTalkId, talkLike} from "@/api/requests/Talk"
import {useSettingStore, useUserInfoStore, useWebsiteInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {scrollDown} from "@/utils/utils";
import {computed, reactive, ref} from "vue";

const settingStore = useSettingStore();
const websiteInfoStore = useWebsiteInfoStore();
const userInfoStore = useUserInfoStore();
const {loadingFlag, loginFlag} = storeToRefs(settingStore);
const {userInfo} = storeToRefs(userInfoStore);
const {rectangle} = storeToRefs(websiteInfoStore);

const currentPicture = reactive({
    image: "",
    current: 0,
    maxCount: 1,
    pictureName: "说说配图"
})
const talkData: Ref<TalkEntity | null> = ref(null);
// 提交按钮锁
const commentBtnLock = ref(false);

const createTalkData = async (talkId: string) => {
    loadingFlag.value = true;
    talkData.value = null;
    await getTalkByTalkId(talkId).then((resp: ResultObject<TalkEntity>) => {
        if (resp.status && resp.data) {
            talkData.value = resp.data
            if (resp.data.rectangle) {
                rectangle.value = resp.data.rectangle;
            }
            if (!talkData.value?.likeCount) {
                talkData.value!.likeCount = 0
            }
            if (!talkData.value?.commentCount) {
                talkData.value!.commentCount = 0
            }
        }
    }).catch(() => {
        notify({
            text: "说说获取失败，请重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
        scrollDown();
    })
}

const previewHandle = (currentIndex: number) => {
    currentPicture.image = talkData.value?.src[currentIndex] as string;
    if (talkData.value?.src && talkData.value?.src.length >= 1) {
        currentPicture.current = currentIndex;
        currentPicture.maxCount = talkData.value?.src.length as number
    }
    document.body.style.overflow = 'hidden'
}

// 下一张
const nextHandle = () => {
    currentPicture.current++;
    currentPicture.image = talkData.value?.src[currentPicture.current] as string;
}

// 上一张
const prevHandle = () => {
    currentPicture.current--;
    currentPicture.image = talkData.value?.src[currentPicture.current] as string;
}

// 判断是否已经点赞
const isLikeComp = computed(() => {
    return userInfoStore.isLogin && userInfo.value?.talkLikeSet.indexOf(talkData.value!.id) !== -1;
})

// 说说点赞
const likeHandle = async () => {
    // 判断登录
    if (!userInfoStore.isLogin) {
        loginFlag.value = true
        return;
    }
    await talkLike(talkData.value?.id as string).then((resp: ResultObject<null>) => {
        if (resp.status) {
            if (userInfo.value?.talkLikeSet.indexOf(talkData.value?.id as string) !== -1) {
                talkData.value!.likeCount!--;
            } else {
                talkData.value!.likeCount!++;
            }
            userInfoStore.talkLike(talkData.value?.id as string);
        }
    })
}

export {
    talkData,
    currentPicture,
    createTalkData,
    previewHandle,
    nextHandle,
    prevHandle,
    commentBtnLock,
    isLikeComp,
    likeHandle
}