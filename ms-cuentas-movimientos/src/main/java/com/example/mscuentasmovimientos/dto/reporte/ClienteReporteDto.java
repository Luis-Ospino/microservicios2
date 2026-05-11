package com.example.mscuentasmovimientos.dto.reporte;

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
public class ClienteReporteDto {

    private UUID id;
    private String nombre;
    private String identificacion;
    private String direccion;
    private String telefono;
    private LocalDateTime fechaCreacion;
}