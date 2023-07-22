import type { StompConfig, StompHeaders, IMessage, frameCallbackType } from '@stomp/stompjs'
import SockJS from 'sockjs-client/dist/sockjs.min.js';
import { Client } from '@stomp/stompjs'
import { useUserInfoStore, useWebsiteInfoStore } from '@/store'
import { storeToRefs } from 'pinia';
import { notify } from '@kyvg/vue3-notification'

export const useWebSocket = () => {
  const userInfoStore = useUserInfoStore();
  const websiteInfoStore = useWebsiteInfoStore();
  const { userInfo } = storeToRefs(userInfoStore);
  const { changeOnlineCount } = websiteInfoStore;
  const WEBSOCKET_URL = import.meta.env.VITE_WEBSOCKET_URL;

  const stompClient = new Client({
    connectHeaders: {},
    // debug: (str) => {
    //   console.log(str);
    // },
    reconnectDelay: 600000,//重连时间，十分钟
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  } as StompConfig);

  // 用SockJS代替brokenURL
  stompClient.webSocketFactory = () => {
    const url = userInfoStore.hasToken ? `${WEBSOCKET_URL}?Authorization=${userInfo.value?.token}` : WEBSOCKET_URL;
    return new SockJS(url, null)
  }
  return {
    stompClient: stompClient,
    connect(callback: frameCallbackType) {
      stompClient.onConnect = (frame) => {
        callback(frame);
        // 订阅在线人数
        this.subscribe("/system/onlineCount", (result) => {
          changeOnlineCount(result as any)
        })
      };
      // 错误
      stompClient.onStompError = (frame) => {
        notify({
          text: "websocket连接失败，请重试",
          type: "warm"
        });
      };
      // 启动
      stompClient.activate();
    },
    // 关闭
    close() {
      if (this.stompClient !== null) {
        return this.stompClient.deactivate();
      }
    },
    // 发送消息
    send(addr: string, msg?: string | null, headers?: StompHeaders) {
      this.stompClient.publish({
        destination: `/app${addr}`,
        body: msg as string,
        headers: headers
      })
    },
    // 订阅消息
    subscribe(addr: string, callback: frameCallbackType) {
      return this.stompClient.subscribe(addr, (res: IMessage) => {
        let result = JSON.parse(res.body)
        callback(result);
      })
    }
  }
}
