package cn.apecode.mail.consumer;

import cn.apecode.common.utils.CommonUtils;
import cn.apecode.dto.EmailDto;
import cn.apecode.entity.MailLog;
import cn.apecode.mapper.MailLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @description: 发送邮件
 * @author: apecode
 * @date: 2022-06-09 20:50
 **/
@RequiredArgsConstructor
@Component
public class ConsumerCommon {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private final MailLogMapper mailLogMapper;

    @Async
    public void sendEmail(EmailDto emailDto, MessageProperties messageProperties) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // 防止成为垃圾邮件
        mimeMessage.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(emailDto.getEmail());
        helper.setSubject(emailDto.getSubject());
        helper.setSentDate(new Date());
        helper.setText(emailDto.getText(), emailDto.getIsHtml());
        javaMailSender.send(mimeMessage);
        MailLog mailLog = MailLog.builder()
                .msgId(messageProperties.getCorrelationId())
                .email(emailDto.getEmail())
                .type(emailDto.getType())
                .topicId(emailDto.getTopicId())
                .count(1)
                .status(1)
                .routeKey(messageProperties.getReceivedRoutingKey())
                .exchange(messageProperties.getReceivedExchange())
                .tryTime(CommonUtils.getLocalDateTime())
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        mailLogMapper.insert(mailLog);
    }

}
