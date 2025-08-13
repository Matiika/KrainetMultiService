package com.Krainet.UserService.service;

import com.Krainet.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Value("${notification.service.url:http://notification-service:8082}")
    private String notificationServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void notifyAdminsUserCreated(String username, String email) {
        String message = String.format("Создан пользователь с именем - %s и почтой - %s.", username, email);
        String subject = String.format("Создан пользователь %s", username);
        sendNotificationToAdmins(subject, message);
    }

    public void notifyAdminsUserUpdated(String username, String email) {
        String message = String.format("Изменен пользователь с именем - %s и почтой - %s.", username, email);
        String subject = String.format("Изменен пользователь %s", username);
        sendNotificationToAdmins(subject, message);
    }

    public void notifyAdminsUserDeleted(String username, String email) {
        String message = String.format("Удален пользователь с именем - %s и почтой - %s.", username, email);
        String subject = String.format("Удален пользователь %s", username);
        sendNotificationToAdmins(subject, message);
    }

    private void sendNotificationToAdmins(String subject, String message) {
        try {
            List<String> adminEmails = userRepository.findAdminEmails();

            if (adminEmails.isEmpty()) {
                System.out.println("Нет администраторов для отправки уведомлений");
                return;
            }

            // Формируем список: [subject, message, email1, email2, ...]
            List<String> notificationData = new ArrayList<>();
            notificationData.add(subject);
            notificationData.add(message);
            notificationData.addAll(adminEmails);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<String>> request = new HttpEntity<>(notificationData, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    notificationServiceUrl + "/api/notifications/send",
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Уведомление успешно отправлено администраторам");
            } else {
                System.err.println("Ошибка при отправке уведомления: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Ошибка при отправке уведомления: " + e.getMessage());
        }
    }

}
