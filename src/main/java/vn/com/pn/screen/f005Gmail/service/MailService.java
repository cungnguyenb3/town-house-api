package vn.com.pn.screen.f005Gmail.service;

import org.springframework.mail.MailException;

import javax.mail.MessagingException;

public interface MailService {
    void sendEmail(String emailAddress, String emailSubject, StringBuilder text) throws MailException;
    void sendEmailWithAttachment(String emailAddress)  throws MailException, MessagingException;
}
