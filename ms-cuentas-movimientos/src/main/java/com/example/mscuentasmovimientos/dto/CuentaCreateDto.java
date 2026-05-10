package com.example.mscuentasmovimientos.dto;

import com.example.mscuentasmovimientos.entity.enums.EstadoCuenta;
import com.example.mscuentasmovimientos.entity.enums.TipoCuenta;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaCreateDto {

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(min = 10, max = 20, message = "El número de cuenta debe tener entre 10 y 20 caracteres")
    private String numeroCuenta;

    @NotNull(message = "El tipo de cuenta es requerido")
    private TipoCuenta tipoCuenta;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
    @Digits(integer = 15, fraction = 2, message = "El saldo debe tener máximo 15 dígitos enteros y 2 decimales")
    private BigDecimal saldoInicial;

    @NotNull(message = "El ID del cliente es requerido")
    private UUID clienteId;

    @NotNull(message = "El estado es requerido")
    @Builder.Default
    private EstadoCuenta estado = EstadoCuenta.ACTIVA;
}