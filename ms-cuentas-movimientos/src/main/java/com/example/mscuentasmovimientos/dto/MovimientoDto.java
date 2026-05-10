package com.example.mscuentasmovimientos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDto {

    private Long id;
    private LocalDateTime fechaMovimiento;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private Long cuentaId;
    private LocalDateTime fechaCreacion;
}