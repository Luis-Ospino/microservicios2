package com.example.msclientespersonas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {

    private Long id;
    private String nombre;
    private String identificacion;
    private String direccion;
    private String telefono;
    private Boolean estado;
    private LocalDateTime fechaCreacion;
}