package com.bulanda.shop.config;

import com.bulanda.shop.sender.NotificationSender;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class SenderConfig {

    @Bean
    public Queue hello() {
        return QueueBuilder.durable("notification").quorum().build();
    }

    @Profile("sender")
    @Bean
    public NotificationSender notificationSender(RabbitTemplate template, Queue notificationQueue) {
        return new NotificationSender(template, notificationQueue);
    }
}
