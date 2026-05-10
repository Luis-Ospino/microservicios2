package com.example.mscuentasmovimientos.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovimientoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void send(String queueName, Object message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
}