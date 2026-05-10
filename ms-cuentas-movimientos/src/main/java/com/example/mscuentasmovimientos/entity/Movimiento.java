package com.example.mscuentasmovimientos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    @Column(nullable = false, length = 10)
    private String tipoMovimiento; // DEBITO, CREDITO

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    private Long cuentaId;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}