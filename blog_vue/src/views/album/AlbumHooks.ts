import type {Ref} from "vue";
import {getAlbums} from "@/api/requests/Album"
import {useSettingStore} from "@/store"
import {storeToRefs} from "pinia";
import {notify} from "@kyvg/vue3-notification"
import {reactive, ref} from "vue";
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore();
const {loadingFlag} = storeToRefs(settingStore);

const albumsData: Ref<{ count: number, recordList: Array<AlbumEntity> } | null> = ref(null);
// 分页
const pagination = reactive({
    size: 10,
    page: 0,
    current: 1
})

const createAlbumsData = async (condition: ConditionParams) => {
    loadingFlag.value = true
    albumsData.value = null;
    await getAlbums(condition).then((resp: PageResult<Array<AlbumEntity>>) => {
        if (resp.status) {
            albumsData.value = resp.data;
            pagination.page = resp.data.count;
            pagination.current = 1;
        }
    }).catch(() => {
        notify({
            text: "相册获取失败，请重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
        scrollDown();
    })
}

const updateAlbumsData = async (condition: ConditionParams) => {
    loadingFlag.value = true
    await getAlbums(condition).then((resp: PageResult<Array<AlbumEntity>>) => {
        if (resp.status) {
            albumsData.value?.recordList.push(...resp.data.recordList);
        }
    }).catch(() => {
        notify({
            text: "相册获取失败，请重试",
            type: "warn"
        });
    }).finally(() => {
        loadingFlag.value = false;
    })
}

// 加载更多
const moreAlbumHandle = async () => {
    if (albumsData.value?.recordList.length !== albumsData.value?.count) {
        pagination.current++;
        await updateAlbumsData(pagination);
    }
}

export {
    pagination,
    albumsData,
    createAlbumsData,
    updateAlbumsData,
    moreAlbumHandle
}