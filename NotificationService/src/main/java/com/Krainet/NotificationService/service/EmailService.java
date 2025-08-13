package com.Krainet.NotificationService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmailToMultipleRecipients(String subject, String messageText, List<String> toEmails) {
        try {
            for (String email : toEmails) {
                sendEmail(subject, messageText, email);
            }
            System.out.println("Уведомления отправлены на " + toEmails.size() + " адресов");
        } catch (Exception e) {
            System.err.println("Ошибка при отправке уведомлений: " + e.getMessage());
            throw new RuntimeException("Не удалось отправить уведомления", e);
        }
    }

    private void sendEmail(String subject, String messageText, String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(messageText);

            mailSender.send(message);
            System.out.println("Email отправлен на: " + toEmail);
        } catch (Exception e) {
            System.err.println("Ошибка при отправке email на " + toEmail + ": " + e.getMessage());
            throw e;
        }
    }

}
