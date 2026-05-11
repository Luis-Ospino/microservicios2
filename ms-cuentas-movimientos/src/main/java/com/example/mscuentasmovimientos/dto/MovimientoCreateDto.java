package com.example.mscuentasmovimientos.dto;

import com.example.mscuentasmovimientos.entity.enums.TipoMovimiento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoCreateDto {

    @NotNull(message = "La fecha es requerida")
    private LocalDateTime fecha;

    @NotNull(message = "El tipo de movimiento es requerido")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "-999999999999.99", inclusive = false, message = "El valor debe ser mayor a -999999999999.99")
    @DecimalMax(value = "999999999999.99", inclusive = false, message = "El valor debe ser menor a 999999999999.99")
    @Digits(integer = 15, fraction = 2, message = "El valor debe tener máximo 15 dígitos enteros y 2 decimales")
    private BigDecimal valor;

    @NotNull(message = "El ID de la cuenta es requerido")
    private UUID cuentaId;
}