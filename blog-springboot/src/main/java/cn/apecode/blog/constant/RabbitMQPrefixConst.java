package cn.apecode.blog.constant;

/**
 * @description: RabbitMQ前缀常量
 * @author: apecode
 * @date: 2022-06-09 19:10
 **/
public class RabbitMQPrefixConst {
    /**
     * Email交换机
     */
    public static final String EMAIL_EXCHANGE_NAME = "email.exchange";

    /**
     * Email队列
     */
    public static final String EMAIL_QUEUE_NAME = "email.queue";

    /**
     * 路由键
     */
    public static final String EMAIL_ROUTING_KEY_NAME = "email.routing.key";
}
