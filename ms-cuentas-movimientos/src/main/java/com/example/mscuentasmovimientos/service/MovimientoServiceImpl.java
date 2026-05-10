package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.MovimientoCreateDto;
import com.example.mscuentasmovimientos.dto.MovimientoDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.entity.Movimiento;
import com.example.mscuentasmovimientos.exception.ResourceNotFoundException;
import com.example.mscuentasmovimientos.mapper.MovimientoMapper;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import com.example.mscuentasmovimientos.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    @Override
    public MovimientoDto createMovimiento(MovimientoCreateDto movimientoCreateDto) {
        // Verificar que la cuenta existe
        Cuenta cuenta = cuentaRepository.findById(movimientoCreateDto.getCuentaId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con id=" + movimientoCreateDto.getCuentaId()));

        // Calcular el nuevo saldo basado en el tipo de movimiento
        BigDecimal nuevoSaldo = calcularNuevoSaldo(cuenta.getSaldoDisponible(), movimientoCreateDto);

        // Crear el movimiento
        Movimiento movimiento = movimientoMapper.toEntity(movimientoCreateDto);
        movimiento.setSaldo(nuevoSaldo);

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        // Actualizar el saldo de la cuenta
        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);

        return movimientoMapper.toDto(movimientoGuardado);
    }

    private BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, MovimientoCreateDto movimientoCreateDto) {
        switch (movimientoCreateDto.getTipoMovimiento()) {
            case DEPOSITO:
            case TRANSFERENCIA_ENTRADA:
                return saldoActual.add(movimientoCreateDto.getValor());
            case RETIRO:
            case TRANSFERENCIA_SALIDA:
            case PAGO_SERVICIOS:
                if (saldoActual.compareTo(movimientoCreateDto.getValor()) < 0) {
                    throw new IllegalArgumentException("Saldo insuficiente para realizar el movimiento");
                }
                return saldoActual.subtract(movimientoCreateDto.getValor());
            case INTERES:
                return saldoActual.add(movimientoCreateDto.getValor());
            default:
                throw new IllegalArgumentException("Tipo de movimiento no soportado");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDto> findAllMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoDto findMovimientoById(UUID id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id=" + id));
        return movimientoMapper.toDto(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDto> findMovimientosByCuentaId(UUID cuentaId) {
        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId)
                .stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDto> findMovimientosByCuentaIdAndFechaBetween(UUID cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByCuentaIdAndFechaBetween(cuentaId, fechaInicio, fechaFin)
                .stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMovimiento(UUID id) {
        if (!movimientoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movimiento no encontrado con id=" + id);
        }
        movimientoRepository.deleteById(id);
    }
}