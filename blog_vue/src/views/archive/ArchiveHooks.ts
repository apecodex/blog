import type {Ref} from "vue"
import {getArticleArchives} from "@/api/requests/Article"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {ref} from "vue";
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore();
const {loadingFlag} = storeToRefs(settingStore);

const archivesData: Ref<Array<ArchiveEntity> | null> = ref(null);

const createArchives = async () => {
    loadingFlag.value = true;
    await getArticleArchives().then((resp: ResultObject<Array<ArchiveEntity>>) => {
        if (resp.status) {
            archivesData.value = resp.data;
        }
    }).catch(() => {
        notify({
            text: "归档获取失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
        scrollDown();
    })
}


export {
    archivesData,
    createArchives
}