package com.pickler.emailsender.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDto(@Email @NotBlank String emailFrom,
                       @Email @NotBlank String emailTo,
                       @NotBlank String subject,
                       @NotBlank String text) {

    @Override
    public String toString() {
        return "EmailDto{" +
                "emailFrom='" + emailFrom + '\'' +
                ", emailTo='" + emailTo + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
