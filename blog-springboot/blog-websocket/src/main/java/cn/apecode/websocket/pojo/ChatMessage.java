package cn.apecode.websocket.pojo;

import cn.apecode.dto.SimpleUserInfoDto;
import cn.apecode.websocket.enums.ChatMessageTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author apecode
 * @description ChatMessage
 * @date 20/6/2023 PM12:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(value = "聊天信息", description = "聊天信息")
public class ChatMessage<T> {

    @ApiModelProperty(value = "发送者", name = "sender", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto sender;

    @ApiModelProperty(value = "消息类型", name = "type", dataType = "ChatMessageTypeEnum")
    private ChatMessageTypeEnum type;

    @ApiModelProperty(value = "发送时间", name = "time", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime time = LocalDateTime.now();

    @ApiModelProperty(value = "ip归属", name = "ipSource", dataType = "String")
    private String ipSource;

    @ApiModelProperty(value = "消息", name = "data", dataType = "Object")
    private T data;

}
