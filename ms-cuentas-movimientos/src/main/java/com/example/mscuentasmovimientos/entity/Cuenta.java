package com.example.mscuentasmovimientos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String numeroCuenta;

    @Column(nullable = false, length = 10)
    private String tipoCuenta; // AHORROS, CORRIENTE

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(nullable = false)
    private Long clienteId;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}