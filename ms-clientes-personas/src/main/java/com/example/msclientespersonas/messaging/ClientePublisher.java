package com.example.msclientespersonas.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void send(String queueName, Object message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
}