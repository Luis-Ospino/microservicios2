package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.reporte.*;
import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.entity.Movimiento;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import com.example.mscuentasmovimientos.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Override
    @Transactional(readOnly = true)
    public ReporteDto generarReporte(UUID clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Generando reporte para cliente ID: {}, fechas: {} - {}", clienteId, fechaInicio, fechaFin);

        // Obtener cliente (en un escenario real, esto vendría de otro microservicio)
        ClienteReporteDto cliente = obtenerCliente(clienteId);

        // Obtener cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        log.debug("Encontradas {} cuentas para el cliente", cuentas.size());

        // Convertir cuentas a DTOs con sus movimientos
        List<CuentaReporteDto> cuentasDto = cuentas.stream()
                .map(cuenta -> convertirCuentaADto(cuenta, fechaInicio, fechaFin))
                .collect(Collectors.toList());

        // Obtener todos los movimientos del período
        List<Movimiento> movimientos = movimientoRepository
                .findByCuentaIdInAndFechaBetween(
                    cuentas.stream().map(Cuenta::getId).collect(Collectors.toList()),
                    fechaInicio, fechaFin);

        List<MovimientoReporteDto> movimientosDto = movimientos.stream()
                .map(this::convertirMovimientoADto)
                .collect(Collectors.toList());

        // Calcular totales
        BigDecimal saldoInicialTotal = cuentas.stream()
                .map(Cuenta::getSaldoInicial)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoDisponibleTotal = cuentas.stream()
                .map(Cuenta::getSaldoDisponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ReporteDto reporte = ReporteDto.builder()
                .cliente(cliente)
                .cuentas(cuentasDto)
                .movimientos(movimientosDto)
                .saldoInicialTotal(saldoInicialTotal)
                .saldoDisponibleTotal(saldoDisponibleTotal)
                .build();

        log.info("Reporte generado exitosamente para cliente ID: {}", clienteId);
        return reporte;
    }

    private ClienteReporteDto obtenerCliente(UUID clienteId) {
        // En un escenario real, esto haría una llamada REST al microservicio de clientes
        // Por ahora, retornamos un DTO mock
        return ClienteReporteDto.builder()
                .id(clienteId)
                .nombre("Cliente " + clienteId.toString().substring(0, 8))
                .identificacion("ID-" + clienteId.toString().substring(0, 8))
                .direccion("Dirección del cliente")
                .telefono("555-0123")
                .fechaCreacion(LocalDateTime.now().minusDays(30))
                .build();
    }

    private CuentaReporteDto convertirCuentaADto(Cuenta cuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Obtener movimientos de esta cuenta en el período
        List<Movimiento> movimientosCuenta = movimientoRepository
                .findByCuentaIdAndFechaBetween(cuenta.getId(), fechaInicio, fechaFin);

        List<MovimientoReporteDto> movimientosDto = movimientosCuenta.stream()
                .map(this::convertirMovimientoADto)
                .collect(Collectors.toList());

        return CuentaReporteDto.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .estado(cuenta.getEstado())
                .saldoInicial(cuenta.getSaldoInicial())
                .saldoDisponible(cuenta.getSaldoDisponible())
                .fechaCreacion(cuenta.getCreatedAt())
                .movimientos(movimientosDto)
                .build();
    }

    private MovimientoReporteDto convertirMovimientoADto(Movimiento movimiento) {
        // Obtener el número de cuenta (en un escenario real, esto podría optimizarse)
        Cuenta cuenta = cuentaRepository.findById(movimiento.getCuentaId()).orElse(null);
        String numeroCuenta = cuenta != null ? cuenta.getNumeroCuenta() : "N/A";

        return MovimientoReporteDto.builder()
                .id(movimiento.getId())
                .fecha(movimiento.getFecha())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .valor(movimiento.getValor())
                .saldo(movimiento.getSaldo())
                .cuentaId(movimiento.getCuentaId())
                .numeroCuenta(numeroCuenta)
                .build();
    }
}