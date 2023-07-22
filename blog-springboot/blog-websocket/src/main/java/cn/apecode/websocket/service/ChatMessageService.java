package cn.apecode.websocket.service;

import cn.apecode.websocket.pojo.OnlineUser;

import java.security.Principal;
import java.util.List;

/**
 * @author apecode
 * @description 聊天室服务类
 * @date 2/7/2023 PM6:40
 */
public interface ChatMessageService {

    /**
     * @description: 添加在线用户
     * @param onlineUserVo
     * @auther apecode
     * @date 2/7/2023 PM6:58
    */
    void addUser(OnlineUser onlineUser);

    /**
     * @description: 删除在线用户
     * @param uid
     * @auther apecode
     * @date 2/7/2023 PM8:04
    */
    void remove(String uid);

    /**
     * @param
     * @return
     * @description: 获取在线用户列表
     * @auther apecode
     * @date 2023/7/20 15:41
     */
    List<OnlineUser> getOnlineUserList();

    /**
     * @description: 获取在线用户列表
     * @auther apecode
     * @date 2023/7/21 0:49
    */
    void sendOnlineUserList();

    /**
     * @param message
     * @param user
     * @description: 发送聊天内容
     * @auther apecode
     * @date 2023/7/20 18:24
     */
    void sendChatMessage(String message, Principal user);

    /**
     * @param type
     * @param user
     * @description: 加入或离开聊天室
     * @auther apecode
     * @date 2023/7/20 21:19
     */
    void joinOrLeaveChatRoom(Integer type, Principal user);
}
