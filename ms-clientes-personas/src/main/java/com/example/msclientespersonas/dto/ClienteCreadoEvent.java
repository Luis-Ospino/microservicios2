package com.example.msclientespersonas.dto;

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
public class ClienteCreadoEvent {

    private UUID clienteId;
    private UUID personaId;
    private String contrasena;
    private LocalDateTime fechaCreacion;
    private String eventoTipo = "cliente.creado";
}