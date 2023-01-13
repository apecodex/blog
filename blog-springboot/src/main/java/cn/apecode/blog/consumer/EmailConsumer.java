package cn.apecode.blog.consumer;

import cn.apecode.blog.dto.EmailDto;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cn.apecode.blog.constant.RabbitMQPrefixConst.EMAIL_QUEUE_NAME;

/**
 * @description: 发送验证码
 * @author: apecode
 * @date: 2022-06-09 20:49
 **/
@Component
public class EmailConsumer {

    @Autowired
    private ConsumerCommon consumerCommon;

    @RabbitListener(queues = EMAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        EmailDto emailDto = ((EmailDto) JSON.parseObject(message.getBody(), EmailDto.class));
        try {
            consumerCommon.sendEmail(emailDto, messageProperties);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            channel.basicNack(tag, false, false);
        }
    }
}
