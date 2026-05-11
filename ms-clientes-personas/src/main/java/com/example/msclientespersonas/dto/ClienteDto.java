package com.example.msclientespersonas.dto;

import com.example.msclientespersonas.entity.enums.EstadoCliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDto {

    private UUID id;
    private UUID personaId;
    private String contrasena;
    private EstadoCliente estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}