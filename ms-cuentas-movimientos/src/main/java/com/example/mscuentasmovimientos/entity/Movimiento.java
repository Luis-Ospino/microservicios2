package com.example.mscuentasmovimientos.entity;

import com.example.mscuentasmovimientos.entity.enums.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "movimientos", indexes = {
    @Index(name = "idx_movimientos_cuenta_id", columnList = "cuenta_id"),
    @Index(name = "idx_movimientos_fecha", columnList = "fecha"),
    @Index(name = "idx_movimientos_tipo", columnList = "tipo_movimiento")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Movimiento extends AuditableEntity {

    @NotNull(message = "La fecha es requerida")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @NotNull(message = "El tipo de movimiento es requerido")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 30)
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "-999999999999.99", inclusive = false, message = "El valor debe ser mayor a -999999999999.99")
    @DecimalMax(value = "999999999999.99", inclusive = false, message = "El valor debe ser menor a 999999999999.99")
    @Digits(integer = 15, fraction = 2, message = "El valor debe tener máximo 15 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "El saldo es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo no puede ser negativo")
    @Digits(integer = 15, fraction = 2, message = "El saldo debe tener máximo 15 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @NotNull(message = "El ID de la cuenta es requerido")
    @Column(name = "cuenta_id", nullable = false, columnDefinition = "UUID")
    private UUID cuentaId;
}