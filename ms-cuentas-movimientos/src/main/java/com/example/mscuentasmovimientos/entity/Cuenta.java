package com.example.mscuentasmovimientos.entity;

import com.example.mscuentasmovimientos.entity.enums.EstadoCuenta;
import com.example.mscuentasmovimientos.entity.enums.TipoCuenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cuentas", indexes = {
    @Index(name = "idx_cuentas_numero", columnList = "numero_cuenta", unique = true),
    @Index(name = "idx_cuentas_cliente_id", columnList = "cliente_id"),
    @Index(name = "idx_cuentas_estado", columnList = "estado")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Cuenta extends AuditableEntity {

    @NotBlank(message = "El número de cuenta no puede estar vacío")
    @Size(min = 10, max = 20, message = "El número de cuenta debe tener entre 10 y 20 caracteres")
    @Column(name = "numero_cuenta", nullable = false, length = 20, unique = true)
    private String numeroCuenta;

    @NotNull(message = "El tipo de cuenta es requerido")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private TipoCuenta tipoCuenta;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
    @Digits(integer = 15, fraction = 2, message = "El saldo debe tener máximo 15 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @NotNull(message = "El saldo disponible es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo disponible no puede ser negativo")
    @Digits(integer = 15, fraction = 2, message = "El saldo debe tener máximo 15 dígitos enteros y 2 decimales")
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal saldoDisponible = BigDecimal.ZERO;

    @NotNull(message = "El estado es requerido")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoCuenta estado = EstadoCuenta.ACTIVA;

    @NotNull(message = "El ID del cliente es requerido")
    @Column(name = "cliente_id", nullable = false, columnDefinition = "UUID")
    private UUID clienteId;
}