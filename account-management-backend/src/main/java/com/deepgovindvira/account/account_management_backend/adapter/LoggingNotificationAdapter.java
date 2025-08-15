package com.deepgovindvira.account.account_management_backend.adapter;

import com.deepgovindvira.account.account_management_backend.port.NotificationPort;
import org.springframework.stereotype.Component;

@Component
public class LoggingNotificationAdapter implements NotificationPort {

    @Override
    public void notify(String recipient, String message) {
        System.out.println("[DEV-NOTIFICATION] To=" + recipient + "\n" + message + "\n");
    }
}
