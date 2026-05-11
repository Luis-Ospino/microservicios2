package com.example.mscuentasmovimientos.messaging;

import com.example.mscuentasmovimientos.config.RabbitMqConfig;
import com.example.mscuentasmovimientos.dto.ClienteCreadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteEventConsumer {

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void handleClienteCreado(ClienteCreadoEvent event) {
        try {
            log.info("Recibido evento cliente.creado: clienteId={}, personaId={}",
                    event.getClienteId(), event.getPersonaId());

            // Aquí podríamos implementar lógica para crear una cuenta por defecto
            // cuando se crea un cliente, o cualquier otra lógica de negocio
            // Por ahora, solo registramos el evento

            log.info("Evento cliente.creado procesado exitosamente");

        } catch (Exception e) {
            log.error("Error al procesar evento cliente.creado: {}", e.getMessage(), e);
            // En un escenario real, podríamos implementar reintentos o dead letter queue
            throw e; // Re-lanzar para que RabbitMQ maneje el mensaje fallido
        }
    }
}