import type { Ref } from 'vue'
import { ref, reactive } from 'vue'
import { useUserInfoStore, useWebSocketStore, useWebsiteInfoStore } from "@/store"
import { storeToRefs } from 'pinia';
import { parseComment } from '@/utils/utils'

const userInfoStore = useUserInfoStore();
const webstieInfoStore = useWebsiteInfoStore();
const webSocketStore = useWebSocketStore();
const { userInfo } = storeToRefs(userInfoStore);
const { hasVisitorUser, visitorUser } = storeToRefs(webSocketStore)
const { websiteInfo } = webstieInfoStore;

// 消息列表
const msgList: Array<ChatMessage<ResultObject<string>>> = reactive([]);
// 在线用户列表
const onlineUserList: Ref<Array<OnlineUser> | null> = ref(null);

// 判断该消息是否由本人发送
const determineUser = (uid: string) => {
  // 用户发送
  if (userInfoStore.isLogin) {
    return userInfo.value?.uid === uid;
  }
  // 游客发送
  if (hasVisitorUser) {
    return visitorUser.value?.uid === uid;
  }
}

// 获取聊天室公告
const getChatRoomNotice = () => {
  return parseComment(websiteInfo?.chatRoomNotice as string, '24', '24');
}

export {
  msgList,
  determineUser,
  onlineUserList,
  getChatRoomNotice
}