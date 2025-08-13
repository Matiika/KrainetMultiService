package com.Krainet.NotificationService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloFromNotificationTest {

    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello from notification";
    }

}
