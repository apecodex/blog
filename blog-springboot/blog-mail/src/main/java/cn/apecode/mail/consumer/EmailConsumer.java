package cn.apecode.mail.consumer;

import cn.apecode.dto.EmailDto;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cn.apecode.common.constant.RabbitMQPrefixConst.EMAIL_QUEUE_NAME;

/**
 * @description: 发送验证码
 * @author: apecode
 * @date: 2022-06-09 20:49
 **/
@RequiredArgsConstructor
@Component
public class EmailConsumer {

    private final ConsumerCommon consumerCommon;

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
