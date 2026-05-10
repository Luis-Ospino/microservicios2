package com.example.msclientespersonas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreateDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20, message = "La identificación no puede exceder 20 caracteres")
    private String identificacion;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 100, message = "La dirección no puede exceder 100 caracteres")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 100, message = "La contraseña no puede exceder 100 caracteres")
    private String contrasena;
}