package com.deepgovindvira.account.account_management_backend.port;

public interface NotificationPort {
    void notify(String recipient, String message);
}
