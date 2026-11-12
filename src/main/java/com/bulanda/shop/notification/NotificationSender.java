package com.bulanda.shop.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class NotificationSender {

    private final RabbitTemplate template;
    private final Queue queue;

    public void send(String message) {
        this.template.convertAndSend(queue.getName(), message);
        log.info("Sent '{}'", message);
        System.out.println(" [x] Sent '" + message + "'");
    }

}
