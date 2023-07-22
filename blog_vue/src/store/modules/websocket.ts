import type { Ref } from "vue";
import { ref, computed } from "vue";
import Name from "./store-name"
import { defineStore } from "pinia";
import { useWebSocket } from "@/utils/WebSocketUtils"

export const useWebSocketStore = defineStore(Name.WEBSOCKET, () => {
  const socket = useWebSocket();

  // 加入聊天室
  const isJoinChat: Ref<boolean> = ref(false);

  // 游客信息
  const visitorUser: Ref<VisitorUser | null> = ref(null);

  // 是否有游客
  const hasVisitorUser = computed(() => visitorUser.value === null);

  // 添加游客
  const addVisitorUser = (user: VisitorUser) => {
    visitorUser.value = user;
  }

  // 删除游客
  const removeVisitorUser = () => {
    visitorUser.value = null;
  }

  // 连接websocket
  const socketConnect = () => {
    socket.connect(() => { })
  }

  return {
    isJoinChat,
    socket,
    socketConnect,
    visitorUser,
    hasVisitorUser,
    addVisitorUser,
    removeVisitorUser
  }
}, { persist: { storage: sessionStorage } })