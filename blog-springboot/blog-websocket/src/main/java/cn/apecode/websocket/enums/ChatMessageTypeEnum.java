package cn.apecode.websocket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apecode
 * @description 聊天消息类型枚举
 * @date 20/6/2023 PM1:04
 */
@Getter
@AllArgsConstructor
public enum ChatMessageTypeEnum {

    CHAT(1, "聊天"),
    JOIN(2, "加入"),
    LEAVE(3, "离开");

    private final Integer type;
    private final String desc;

    public static ChatMessageTypeEnum getChatMessageTypeEnum(Integer type) {
        for (ChatMessageTypeEnum value : ChatMessageTypeEnum.values()) {
            if (value.type.equals(type)) return value;
        }
        return null;
    }
}
