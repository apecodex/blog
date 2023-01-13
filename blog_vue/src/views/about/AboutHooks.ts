import type {Ref} from "vue"
import {ref} from "vue"
import {getAbout} from "@/api/requests/WebsiteInfo"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore();
const {loadingFlag} = storeToRefs(settingStore);

const aboutData: Ref<string | null> = ref(null);

const createAboutData = async () => {
    loadingFlag.value = true;
    aboutData.value = null;
    await getAbout().then((resp: ResultObject<string>) => {
        if (resp.status) {
            aboutData.value = resp.data
        }
    }).catch(() => {
        notify({
            text: "数据获取失败，请重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
        scrollDown();
    })
}

export {
    aboutData,
    createAboutData
}