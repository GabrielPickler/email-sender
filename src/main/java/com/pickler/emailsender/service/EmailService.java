package com.pickler.emailsender.service;

import com.pickler.emailsender.domain.Email;
import com.pickler.emailsender.enums.EmailStatus;
import com.pickler.emailsender.repository.EmailRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LogManager.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    private final EmailRepository repository;

    public EmailService(JavaMailSender javaMailSender, EmailRepository repository) {
        this.javaMailSender = javaMailSender;
        this.repository = repository;
    }

    public void sendEmail(Email email) {
        final SimpleMailMessage simpleMailMessage = email.toSimpleMailMessage();

        try {
            javaMailSender.send(simpleMailMessage);
            email.changeStatus(EmailStatus.SENT);
        } catch (MailException exception) {
            email.changeStatus(EmailStatus.FAILED);
            LOGGER.error("Erro ao enviar email", exception);
        } finally {
            repository.save(email);
        }
    }
}
