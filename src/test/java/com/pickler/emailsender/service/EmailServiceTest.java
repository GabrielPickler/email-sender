package com.pickler.emailsender.service;

import com.pickler.emailsender.domain.Email;
import com.pickler.emailsender.enums.EmailStatus;
import com.pickler.emailsender.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService service;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void shouldSaveEmailWithFailedStatusOnMailException() {
        final Email email = new Email("teste@gmail.com", "teste2@gmail.com", "titulo teste", "teste");
        doThrow(new MailSendException("falha no envio")).when(javaMailSender).send(email.toSimpleMailMessage());

        service.sendEmail(email);

        verify(emailRepository, only()).save(email);
        assertEquals(EmailStatus.FAILED, email.getStatus());
    }

    @Test
    void shouldSaveEmailWithSentStatusOnMailSuccess() {
        final Email email = new Email("teste@gmail.com", "teste2@gmail.com", "titulo teste", "teste");

        service.sendEmail(email);

        verify(emailRepository, only()).save(email);
        assertEquals(EmailStatus.SENT, email.getStatus());
    }

}