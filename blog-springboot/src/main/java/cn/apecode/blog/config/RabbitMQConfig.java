package cn.apecode.blog.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cn.apecode.blog.constant.RabbitMQPrefixConst.*;


/**
 * @description: RabbitMQ配置类
 * @author: apecode
 * @date: 2022-06-09 19:10
 **/
@Configuration
public class RabbitMQConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(cachingConnectionFactory);
    }

    @Bean
    public Queue codeEmailQueue() {
        return new Queue(EMAIL_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange codeEmailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding bindingCodeEmailDirect() {
        return BindingBuilder.bind(codeEmailQueue()).to(codeEmailExchange()).with(EMAIL_ROUTING_KEY_NAME);
    }

}
