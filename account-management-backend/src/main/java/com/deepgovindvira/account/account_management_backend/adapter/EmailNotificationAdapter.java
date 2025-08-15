package com.deepgovindvira.account.account_management_backend.adapter;

import com.deepgovindvira.account.account_management_backend.port.NotificationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * To enable email notifications via Spring, uncomment these annotations.
 */
// @Component
// @Primary
public class EmailNotificationAdapter implements NotificationPort {
    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void notify(String recipient, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject("Notification");
        email.setText(message);
        email.setFrom("deepgovindvira@gmail.com");

        mailSender.send(email);

        // demo: just log/print (replace with real email/SMS providers)
        System.out.printf("[EMAIL] to=%s :: %s%n", recipient, message);
    }
}
