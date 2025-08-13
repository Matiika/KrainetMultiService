package com.Krainet.NotificationService.contoller;

import com.Krainet.NotificationService.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Отправить email уведомление",
            description = "Принимает список, где первый элемент - тема, второй - сообщение, остальные - email адреса получателей")
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestBody @Schema(description = "Список: [тема, сообщение, email1, email2, ...]",
                    example = "[\"Создан пользователь test\", \"Создан пользователь с именем test\", \"admin@example.com\"]")
            List<String> notificationData) {

        if (notificationData == null || notificationData.size() < 3) {
            return ResponseEntity.badRequest().body("Некорректные данные. Ожидается: [тема, сообщение, email1, email2, ...]");
        }

        String subject = notificationData.get(0);
        String message = notificationData.get(1);
        List<String> emails = notificationData.subList(2, notificationData.size());

        try {
            emailService.sendEmailToMultipleRecipients(subject, message, emails);
            return ResponseEntity.ok("Уведомления успешно отправлены на " + emails.size() + " адресов");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при отправке уведомлений: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Notification Service работает!");
    }

}
