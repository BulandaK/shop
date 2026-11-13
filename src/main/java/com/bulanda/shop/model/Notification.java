package com.bulanda.shop.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "notifications")
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private Channel channel;
    private String content;
    private Priority priority;
    private ZonedDateTime scheduledTime;
    private NotificationStatus notificationStatus;
}
