package cn.apecode.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author apecode
 * @description WebSocketConfig
 * @date 20/6/2023 AM10:51
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //设置心跳的时间间隔
    private static final long HEART_BEAT = 60000;
    private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                // http握手拦截器
                .addInterceptors(webSocketHandshakeInterceptor)
                // 封装的认证用户信息
                .setHandshakeHandler(new WebSocketHandshakeHandler())
                .withSockJS();
    }

    /**
     * @param registry
     * @description: public：群聊天
     *                       user：用户点对点聊天
     *                       system：系统通知
     * @auther apecode
     * @date 20/6/2023 PM1:59
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 自定义调度器，用于控制心跳线程
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 线程池线程数，心跳连接开线程
        taskScheduler.setPoolSize(1);
        // 线程名前缀
        taskScheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        // 初始化
        taskScheduler.initialize();
        // 设置广播节点
        registry.enableSimpleBroker("/system", "/user", "/public")
                .setHeartbeatValue(new long[]{HEART_BEAT, HEART_BEAT})
                .setTaskScheduler(taskScheduler);
        // 客户端向服务端发送消息需有/app 前缀
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * @param registration
     * @description: 配置客户端入站通道拦截器
     * @auther apecode
     * @date 1/7/2023 PM11:16
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        /*
         * 配置消息线程池
         * 1. corePoolSize 配置核心线程池，当线程数小于此配置时，不管线程中有无空闲的线程，都会产生新线程处理任务
         * 2. maxPoolSize 配置线程池最大数，当线程池数等于此配置时，不会产生新线程
         * 3. keepAliveSeconds 线程池维护线程所允许的空闲时间，单位秒
         */
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
    }

    /**
     * @param registration
     * @description: 设置输出消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
     * @auther apecode
     * @date 1/7/2023 PM11:17
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
    }

}
