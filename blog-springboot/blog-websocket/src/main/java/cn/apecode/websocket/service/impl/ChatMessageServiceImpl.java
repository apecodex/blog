package cn.apecode.websocket.service.impl;

import cn.apecode.common.dto.UserDetailsDto;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.dto.SimpleUserInfoDto;
import cn.apecode.service.RedisService;
import cn.apecode.websocket.enums.ChatMessageTypeEnum;
import cn.apecode.websocket.pojo.ChatMessage;
import cn.apecode.websocket.pojo.OnlineUser;
import cn.apecode.websocket.pojo.VisitorUser;
import cn.apecode.websocket.service.ChatMessageService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.apecode.common.constant.RedisPrefixConst.ONLINE_USER;

/**
 * @author apecode
 * @description WebSocketServiceImpl
 * @date 2/7/2023 PM6:40
 */
@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final RedisService redisService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * @description: 添加在线用户
     * @param onlineUserVo
     * @auther apecode
     * @date 2/7/2023 PM6:59
    */
    @Override
    public void addUser(OnlineUser onlineUser) {
        if (!redisService.hHasKey(ONLINE_USER, onlineUser.getUid())) {
            redisService.hSet(ONLINE_USER, onlineUser.getUid(), JSON.toJSONString(onlineUser));
        }
    }

    /**
     * @description: 删除在线用户
     * @param uid
     * @auther apecode
     * @date 2/7/2023 PM8:04
    */
    @Override
    public void remove(String uid) {
        if (redisService.hHasKey(ONLINE_USER, uid)) {
            redisService.hDel(ONLINE_USER, uid);
        }
    }

    /**
     * @return
     * @description: 获取在线用户列表
     * @auther apecode
     * @date 2023/7/20 15:54
     */
    @Override
    public List<OnlineUser> getOnlineUserList() {
        List<OnlineUser> onlineUserList = null;
        if (redisService.hasKey(ONLINE_USER)) {
            Map<String, Object> onlineUserMap = redisService.hmGet(ONLINE_USER);
            onlineUserList = onlineUserMap.values().stream().map(onlineUser -> JSON.parseObject(onlineUser.toString(), OnlineUser.class)).collect(Collectors.toList());
        }
        return onlineUserList;
    }

    /**
     * @description: 获取在线用户列表
     * @auther apecode
     * @date 2023/7/21 12:00
    */
    public void sendOnlineUserList() {
        List<OnlineUser> onlineUserList = getOnlineUserList();
        messagingTemplate.convertAndSend("/public/onlineUser",  JSON.toJSONString(ResponseCode.ok(onlineUserList)));
    }

    /**
     * @param message
     * @param user
     * @description: 发送聊天内容
     * @auther apecode
     * @date 2023/7/20 18:25
     */
    @Override
    public void sendChatMessage(String message, Principal user) {
        if (StringUtils.isNotBlank(message)) {
            ChatMessage<ResponseCode<String>> chatMessage = getUserInfo(user);
            chatMessage.setType(ChatMessageTypeEnum.CHAT);
            chatMessage.setData(ResponseCode.ok(message));
            messagingTemplate.convertAndSend("/public/chat", JSON.toJSONString(chatMessage));
        }
    }

    /**
     * @param type
     * @param user
     * @description: 加入或离开聊天室
     * @auther apecode
     * @date 2023/7/20 21:23
     */
    @Override
    public void joinOrLeaveChatRoom(Integer type, Principal user) {
        ChatMessage<ResponseCode<String>> chatMessage = getUserInfo(user);
        chatMessage.setType(ChatMessageTypeEnum.getChatMessageTypeEnum(type));
        chatMessage.setData(ResponseCode.ok());
        messagingTemplate.convertAndSend("/public/chat", JSON.toJSONString(chatMessage));
    }

    /**
     * @description: 获取聊天室用户信息
     * @param user
     * @return {@link ChatMessage <ResponseCode<String>>}
     * @auther apecode
     * @date 2023/7/20 15:05
     */
    private ChatMessage<ResponseCode<String>> getUserInfo(Principal user) {
        ChatMessage<ResponseCode<String>> chatMessage = new ChatMessage<>();
        if (user instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) user;
            UserDetailsDto userDetailsDto = (UserDetailsDto) authenticationToken.getPrincipal();
            chatMessage.setSender(SimpleUserInfoDto.builder()
                    .uid(userDetailsDto.getUid())
                    .avatar(userDetailsDto.getAvatar())
                    .nickname(userDetailsDto.getNickname())
                    .build());
            chatMessage.setIpSource(userDetailsDto.getIpSource());
        }
        if (user instanceof VisitorUser) {
            VisitorUser visitorUser = (VisitorUser) user;
            chatMessage.setSender(SimpleUserInfoDto.builder()
                    .uid(visitorUser.getUid())
                    .nickname(visitorUser.getNickname())
                    .avatar(visitorUser.getAvatar())
                    .build());
            chatMessage.setIpSource(visitorUser.getIpSource());
        }
        return chatMessage;
    }
}
