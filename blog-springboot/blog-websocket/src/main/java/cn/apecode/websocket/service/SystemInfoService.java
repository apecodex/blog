package cn.apecode.websocket.service;

/**
 * @description: 系统消息服务类
 * @author: apecode
 * @date: 2023-07-20 17:02
 **/
public interface SystemInfoService {

    /**
     * @description: 获取在线人数
     * @return {@link Integer}
     * @auther apecode
     * @date 2023/7/21 12:04
    */
    Integer getOnlineCount();
}
