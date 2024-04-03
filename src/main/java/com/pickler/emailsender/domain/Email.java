package com.pickler.emailsender.domain;

import com.pickler.emailsender.enums.EmailStatus;
import com.pickler.emailsender.exception.BusinessException;
import jakarta.persistence.*;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Email {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String emailFrom;

    private String emailTo;

    private String subject;

    private String text;

    private final LocalDateTime registerDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    public Email(String emailFrom, String emailTo, String subject, String text) {
        if (!hasValidEmail(emailFrom, emailTo)) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("O assunto do email é obrigatório");
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("O texto do email é obrigatório");
        }

        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.subject = subject;
        this.text = text;
        this.status = EmailStatus.PENDING;
    }

    public SimpleMailMessage toSimpleMailMessage() {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText(this.text);
        simpleMailMessage.setSubject(this.subject);
        simpleMailMessage.setFrom(this.emailFrom);
        simpleMailMessage.setTo(this.emailTo);

        return simpleMailMessage;
    }

    public void changeStatus(EmailStatus newStatus) {
        if (hasEmailBeenSent()) {
            throw new BusinessException("O email já foi enviado.");
        }

        this.status = newStatus;
    }

    private boolean hasEmailBeenSent() {
        return this.status != null && this.status == EmailStatus.SENT;
    }

    private boolean hasValidEmail(String... emails) {
        for (String email : emails) {
            if (email == null || email.isBlank()) {
                return false;
            }

            final Matcher matcher = EMAIL_PATTERN.matcher(email);

            if (!matcher.matches()) {
                return false;
            }
        }

        return true;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }
}
