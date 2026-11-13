package com.bulanda.shop.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
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
