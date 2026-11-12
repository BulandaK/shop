package com.bulanda.shop.service;

import com.bulanda.shop.notification.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationSender notificationSender;

    public void sendNotification() {
        notificationSender.send("powiadomienie !");
    }

}
