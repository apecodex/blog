import type {Ref} from "vue";
import {getPicturesByAlbumId} from "@/api/requests/Picture"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {ref} from "vue";
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore()
const {loadingFlag} = storeToRefs(settingStore);

const picturesData: Ref<{ count: number, recordList: Array<PictureEntity>, data: PictureAlbumInfoEntity } | null> = ref(null);
const pagination = {
    size: 30,
    current: 1
} as ConditionParams;

const createPicturesData = async (condition: ConditionParams) => {
    loadingFlag.value = true
    picturesData.value = null;
    pagination.current = 1;
    await getPicturesByAlbumId(condition).then((resp: PageResultWithObject<Array<PictureEntity>, PictureAlbumInfoEntity>) => {
        if (resp.status) {
            picturesData.value = resp.data
            if (picturesData.value?.data.albumDesc.length === 0) {
                picturesData.value!.data.albumDesc = "关于该相册博主啥也没解释~";
            }
        }
    }).catch(() => {
        notify({
            text: "图片获取失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
        scrollDown();
    })
}
const updatePicturesData = async (condition: ConditionParams) => {
    loadingFlag.value = true
    pagination.current!++;
    await getPicturesByAlbumId(condition).then((resp) => {
        if (resp.status) {
            picturesData.value?.recordList.push(...resp.data.recordList);
        }
    }).catch(() => {
        notify({
            text: "图片获取失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false
    })
}


export {
    pagination,
    picturesData,
    createPicturesData,
    updatePicturesData
}