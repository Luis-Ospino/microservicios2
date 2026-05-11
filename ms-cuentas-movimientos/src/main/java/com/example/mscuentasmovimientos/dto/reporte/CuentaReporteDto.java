package com.example.mscuentasmovimientos.dto.reporte;

import com.example.mscuentasmovimientos.entity.enums.EstadoCuenta;
import com.example.mscuentasmovimientos.entity.enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaReporteDto {

    private UUID id;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private EstadoCuenta estado;
    private BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
    private LocalDateTime fechaCreacion;
    private List<MovimientoReporteDto> movimientos;
}