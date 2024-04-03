package com.pickler.emailsender.consumer;

import com.pickler.emailsender.domain.Email;
import com.pickler.emailsender.dto.EmailDto;
import com.pickler.emailsender.service.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private static final Logger LOGGER = LogManager.getLogger(EmailConsumer.class);

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void listen(@Payload EmailDto emailDto) {
        LOGGER.debug("Enviando email - {}", emailDto);
        final Email email = new Email(emailDto.emailFrom(), emailDto.emailTo(), emailDto.subject(), emailDto.text());
        emailService.sendEmail(email);
    }
}
