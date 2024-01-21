import type {Ref} from "vue";
import {notify} from "@kyvg/vue3-notification"
import {getMessages, saveMessage} from "@/api/requests/Message"
import {reactive, ref} from "vue";
import {useSettingStore, useWebsiteInfoStore} from "@/store"
import {storeToRefs} from "pinia";
import {scrollDown} from "@/utils/utils";

const settingStore = useSettingStore();
const websiteInfoStore = useWebsiteInfoStore();
const {loadingFlag} = storeToRefs(settingStore);
const {websiteInfo} = storeToRefs(websiteInfoStore);

const messagesData: Ref<{count: number, recordList: Array<MessageEntity>} | null> = ref(null);
const currentMessage: Ref<MessageEntity | null> = ref(null);
const pagination = reactive({
    current: 1,
    size: 30
})
const messageForm = reactive({
    theme: "default",
    content: ""
}) as {content: string, theme: MessageTheme}

// 保存留言
const saveMessageHandle = async () => {
    if (messageForm.content.length === 0) {
        notify({
            text: "说点啥吧?~",
            type: "warn"
        });
        return;
    }
    loadingFlag.value = true
    await saveMessage({theme: messageForm.theme, content: messageForm.content}).then((resp: ResultObject<null>) => {
        if (resp.status) {
            // 重新请求最新留言
            if (websiteInfo.value?.isMessageReview) {
                notify({
                    text: "谢谢你的留言，待审核中...",
                    type: "success"
                })
            } else {
                notify({
                    text: "谢谢你的留言~",
                    type: "success"
                })
            }
            createMessagesData(pagination);
            // 初始化数据
            pagination.current = 1;
            messageForm.content = "";
            messageForm.theme = "default"
        }
    }).catch(() => {
        notify({
            text: "留言失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
    })
}

const createMessagesData = async (condition: ConditionParams) => {
    loadingFlag.value = true;
    messagesData.value = null;
    await getMessages(condition).then((resp: PageResult<Array<MessageEntity>>) => {
        if (resp.status) {
            messagesData.value = resp.data;
        }
    }).catch(() => {
        notify({
            text: "获取留言失败，请重试",
            type: "warn"
        })
    }).finally(() => {
        loadingFlag.value = false;
        scrollDown();
    })
}

// 分页
const changePage = async (currentPage: any) => {
    if (currentPage !== pagination.current) {
        pagination.current = currentPage;
        await createMessagesData({current: currentPage, size: pagination.size});
    }
}

// 展示留言内容
const clickMessageItem = (message: MessageEntity) => {
    currentMessage.value = message
    document.body.style.overflow = 'hidden'
}
export {
    pagination,
    messagesData,
    messageForm,
    saveMessageHandle,
    createMessagesData,
    changePage,
    currentMessage,
    clickMessageItem
}