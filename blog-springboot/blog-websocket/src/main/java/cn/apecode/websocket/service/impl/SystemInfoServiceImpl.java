package cn.apecode.websocket.service.impl;

import cn.apecode.service.RedisService;
import cn.apecode.websocket.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static cn.apecode.common.constant.RedisPrefixConst.ONLINE_USER;

/**
 * @description: 系统信息
 * @author: apecode
 * @date: 2023-07-20 17:03
 **/
@Service
@RequiredArgsConstructor
public class SystemInfoServiceImpl implements SystemInfoService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisService redisService;

    /**
     * @description: 获取在线人数
     * @return {@link Integer}
     * @auther apecode
     * @date 2023/7/21 12:16
    */
    @Override
    public Integer getOnlineCount() {
        Long size = 0L;
        if (redisService.hasKey(ONLINE_USER)) {
            size = redisService.hSize(ONLINE_USER);
        }
        messagingTemplate.convertAndSend("/system/onlineCount", size.intValue());
        return size.intValue();
    }
}
