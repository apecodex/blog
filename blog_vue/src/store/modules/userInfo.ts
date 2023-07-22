import Names from "./store-name"
import { defineStore, storeToRefs } from "pinia";
import { computed, ref, Ref } from "vue";
import { notify } from "@kyvg/vue3-notification"
import { router } from "@/router"
import { useWebSocketStore } from "@/store"

export const useUserInfoStore = defineStore(Names.USERINFO, () => {
  const userInfo: Ref<UserEntity | null> = ref(null)
  // 用户通知数量
  const userNoticeCount: Ref<number> = ref(0);

  // 是否绑定QQ（true登录/false绑定）
  const isBindQQ: Ref<boolean> = ref(true);

  // 是否登录
  const isLogin = computed(() => userInfo.value !== null);

  const saveUserInfo = (info: UserEntity) => {
    const webSocketStore = useWebSocketStore();
    const { removeVisitorUser } = webSocketStore;
    userInfo.value = info;
    // 删除游客信息
    removeVisitorUser();
  }

  // 返回Token
  const getToken = () => {
    return userInfo.value?.tokenHead + " " + userInfo.value?.token;
  }

  // 判断Token是否有
  const hasToken = computed(() => userInfo.value && userInfo.value.token.length !== 0)

  // 更新Token
  const updateToken = (newToken: string) => {
    userInfo.value!.token = newToken;
  }

  // 更新邮箱
  const updateEmail = (email: string | null) => {
    userInfo.value!.email = email;
  }

  // 删除用户
  const removeUserInfo = () => {
    const webSocketStore = useWebSocketStore();
    const { removeVisitorUser } = webSocketStore;
    userInfo.value = null;
    // 删除游客信息
    removeVisitorUser();
  }

  // 文章点赞
  const articleLike = (articleId: string) => {
    if (userInfo.value?.articleLikeSet.indexOf(articleId) !== -1) {
      userInfo.value?.articleLikeSet.splice(userInfo.value?.articleLikeSet.indexOf(articleId), 1);
    } else {
      userInfo.value?.articleLikeSet.push(articleId);
    }
  }

  // 评论点赞
  const commentLike = (commentId: string) => {
    if (userInfo.value?.commentLikeSet.indexOf(commentId) !== -1) {
      userInfo.value?.commentLikeSet.splice(userInfo.value?.commentLikeSet.indexOf(commentId), 1);
    } else {
      userInfo.value?.commentLikeSet.push(commentId);
    }
  }

  // 说说点赞
  const talkLike = (talkId: string) => {
    if (userInfo.value?.talkLikeSet.indexOf(talkId) !== -1) {
      userInfo.value?.talkLikeSet.splice(userInfo.value?.talkLikeSet.indexOf(talkId), 1);
    } else {
      userInfo.value?.talkLikeSet.push(talkId);
    }
  }

  const logout = async () => {
    const webSocketStore = useWebSocketStore();
    const { socket, socketConnect, } = webSocketStore;
    const { isJoinChat } = storeToRefs(webSocketStore);
    // 删除用户
    removeUserInfo();
    userNoticeCount.value = 0;
    // 如果在需要登录的页面退出，则 跳转至首页
    if (router.currentRoute.value.meta.isLogin) {
      await router.replace({ path: "/" });
    }
    // QQ退出登录
    if (QC.Login.check()) {
      QC.Login.signOut()
    }

    // 先关闭连接
    socket.close();
    socket.stompClient.onDisconnect = (frame) => {
      // 重新连接WebSocket
      if (!socket.stompClient.connected) {
        socketConnect();
      }
    }
    // 重置加入聊天按钮
    isJoinChat.value = false;
    notify({
      text: "退出成功",
      type: "success"
    });
  }

  return {
    userInfo,
    userNoticeCount,
    isBindQQ,
    isLogin,
    saveUserInfo,
    logout,
    getToken,
    hasToken,
    updateToken,
    updateEmail,
    removeUserInfo,
    articleLike,
    commentLike,
    talkLike
  }
}, { persist: { storage: localStorage } })