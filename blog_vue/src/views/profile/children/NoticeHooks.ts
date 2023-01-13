import type {Ref} from "vue"
import {reactive, ref} from "vue"
import {useSettingStore, useUserInfoStore} from "@/store"
import {getUserNoReadNoticeCount, getUserNotices, updateNotice} from "@/api/requests/Notice";
import {notify} from "@kyvg/vue3-notification"
import {storeToRefs} from "pinia";
import {StatusCode} from "@/api/enum/StatusCode";
import {router} from "@/router";

const settingStore = useSettingStore();
const userInfoStore = useUserInfoStore();
const {loadingFlag} = storeToRefs(settingStore);
const {userNoticeCount} = storeToRefs(userInfoStore);

const noticesData: Ref<{count: number, recordList: Array<NoticeEntity> } | null> = ref(null);
const pagination = reactive({
    isRead: false,
    current: 1,
    size: 10
})

const createUserNoReadCountHandle = async () => {
    await getUserNoReadNoticeCount().then((resp: ResultObject<number>) => {
        if (resp.status) {
            userNoticeCount.value = resp.data;
        }
    })
}

const createNoticesData = async (isRead: boolean, {current, size}: ConditionParams) => {
    loadingFlag.value = true;
    noticesData.value = null;
    pagination.current = 1;
    await getUserNotices(isRead,{current, size}).then((resp: PageResult<Array<NoticeEntity>>) => {
        if (resp.status) {
            noticesData.value = resp.data
        } else {
            if (resp.code === StatusCode.UNAUTHORIZED) {
                router.replace({path: "/"});
            }
            if (resp.code === StatusCode.FAIL) {
                notify({
                    text: resp.message,
                    type: "warn"
                });
            }
        }
    }).catch(() => {
        notify({
            text: "通知获取失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
    })
}

/**
 * 更新通知
 * @param isRead
 * @param current
 * @param size
 */
const updateNoticesData = async (isRead: boolean, {current, size}: ConditionParams) => {
    loadingFlag.value = true;
    await getUserNotices(isRead,{current, size}).then((resp: PageResult<Array<NoticeEntity>>) => {
        if (resp.status) {
            noticesData.value?.recordList.push(...resp.data.recordList)
        } else {
            if (resp.code === StatusCode.UNAUTHORIZED) {
                router.replace({path: "/"});
            }
            if (resp.code === StatusCode.FAIL) {
                notify({
                    text: resp.message,
                    type: "warn"
                });
            }
        }
    }).catch(() => {
        notify({
            text: "通知获取失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
    })
}

const updateNoticeReadHandle = async (noticeIds: Array<string>, allRead: boolean = false) => {
    await updateNotice(noticeIds).then((resp: ResultObject<null>) => {
        if (resp.status) {
            createNoticesData(pagination.isRead, {current: pagination.current, size: pagination.size});
            if (allRead) {
                userNoticeCount.value = 0;
            } else {
                userNoticeCount.value--;
            }
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
            text: "更新失败，请重试",
            type: "warn"
        })
    })
}

const updateNoticesDataHandle = async () => {
    if (noticesData.value!.recordList.length !== noticesData.value!.count) {
        pagination.current++;
        await updateNoticesData(pagination.isRead, {current: pagination.current, size: pagination.size});
    }
}

const changeIsRead = async (isRead: boolean) => {
    if (isRead !== pagination.isRead) {
        pagination.isRead = isRead;
        pagination.current = 1;
        await createNoticesData(pagination.isRead, {current: pagination.current, size: pagination.size})
    }
}

/**
 * 清除未读
 */
const clearNoReadHandle = async () => {
    loadingFlag.value = true;
    await getUserNotices(false, {current: 1, size: noticesData.value?.count}).then((resp: PageResult<Array<NoticeEntity>>) => {
        if (resp.status) {
            const noticeIds = resp.data.recordList.map((notice: NoticeEntity) => notice.id);
            updateNoticeReadHandle(noticeIds, true);
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
            text: "清除失败，请重试",
        })
    }).finally(() => {
        loadingFlag.value = false;
    })
}
export {
    pagination,
    noticesData,
    createNoticesData,
    updateNoticesDataHandle,
    changeIsRead,
    updateNoticeReadHandle,
    createUserNoReadCountHandle,
    clearNoReadHandle
}