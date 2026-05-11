package com.example.msclientespersonas.messaging;

import com.example.msclientespersonas.config.RabbitMqConfig;
import com.example.msclientespersonas.dto.ClienteCreadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarClienteCreado(ClienteCreadoEvent event) {
        try {
            log.info("Enviando evento cliente.creado para cliente ID: {}", event.getClienteId());
            rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_NAME,
                RabbitMqConfig.ROUTING_KEY_CLIENTE_CREADO,
                event
            );
            log.info("Evento cliente.creado enviado exitosamente");
        } catch (Exception e) {
            log.error("Error al enviar evento cliente.creado: {}", e.getMessage(), e);
            // En un escenario real, podríamos implementar retry o dead letter queue
            throw new RuntimeException("Error al publicar evento cliente.creado", e);
        }
    }
}