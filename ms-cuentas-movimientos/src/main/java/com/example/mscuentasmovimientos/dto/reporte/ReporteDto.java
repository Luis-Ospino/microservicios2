package com.example.mscuentasmovimientos.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDto {

    private ClienteReporteDto cliente;
    private List<CuentaReporteDto> cuentas;
    private List<MovimientoReporteDto> movimientos;
    private BigDecimal saldoInicialTotal;
    private BigDecimal saldoDisponibleTotal;
}