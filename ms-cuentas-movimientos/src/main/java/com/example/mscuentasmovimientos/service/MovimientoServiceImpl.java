package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.MovimientoCreateDto;
import com.example.mscuentasmovimientos.dto.MovimientoDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.entity.Movimiento;
import com.example.mscuentasmovimientos.entity.enums.EstadoCuenta;
import com.example.mscuentasmovimientos.entity.enums.TipoMovimiento;
import com.example.mscuentasmovimientos.exception.ResourceNotFoundException;
import com.example.mscuentasmovimientos.exception.SaldoInsuficienteException;
import com.example.mscuentasmovimientos.mapper.MovimientoMapper;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import com.example.mscuentasmovimientos.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    private static final BigDecimal SCALE_TWO = new BigDecimal("0.01");

    @Override
    @Transactional
    public MovimientoDto createMovimiento(MovimientoCreateDto movimientoCreateDto) {
        log.info("Creando movimiento para cuenta ID: {}", movimientoCreateDto.getCuentaId());

        // Validar el movimiento
        validarMovimiento(movimientoCreateDto);

        // Obtener la cuenta con bloqueo pesimista para evitar race conditions
        Cuenta cuenta = cuentaRepository.findByIdForUpdate(movimientoCreateDto.getCuentaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id=" + movimientoCreateDto.getCuentaId()));

        // Verificar que la cuenta esté activa
        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new IllegalStateException("La cuenta no está activa");
        }

        // Calcular el nuevo saldo
        BigDecimal nuevoSaldo = calcularNuevoSaldo(cuenta.getSaldoDisponible(), movimientoCreateDto);

        // Verificar que el saldo no quede negativo
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Intento de movimiento que dejaría saldo negativo. Saldo actual: {}, Valor movimiento: {}",
                    cuenta.getSaldoDisponible(), movimientoCreateDto.getValor());
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        // Crear el movimiento
        Movimiento movimiento = movimientoMapper.toEntity(movimientoCreateDto);
        movimiento.setSaldo(nuevoSaldo.setScale(2, RoundingMode.HALF_UP));

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        // Actualizar el saldo de la cuenta
        cuenta.setSaldoDisponible(nuevoSaldo.setScale(2, RoundingMode.HALF_UP));
        cuentaRepository.save(cuenta);

        log.info("Movimiento creado exitosamente. Nuevo saldo: {}", nuevoSaldo);

        return movimientoMapper.toDto(movimientoGuardado);
    }

    private void validarMovimiento(MovimientoCreateDto dto) {
        // Validar signo del valor basado en el tipo de movimiento
        switch (dto.getTipoMovimiento()) {
            case DEPOSITO:
            case TRANSFERENCIA_ENTRADA:
            case INTERES:
                if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El valor para " + dto.getTipoMovimiento() + " debe ser positivo");
                }
                break;
            case RETIRO:
            case TRANSFERENCIA_SALIDA:
            case PAGO_SERVICIOS:
                if (dto.getValor().compareTo(BigDecimal.ZERO) >= 0) {
                    throw new IllegalArgumentException("El valor para " + dto.getTipoMovimiento() + " debe ser negativo");
                }
                break;
        }

        // Validar que el valor esté dentro de límites razonables
        BigDecimal absValor = dto.getValor().abs();
        if (absValor.compareTo(new BigDecimal("999999999999.99")) > 0) {
            throw new IllegalArgumentException("El valor del movimiento excede el límite permitido");
        }
    }

    private BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, MovimientoCreateDto movimientoCreateDto) {
        // Asegurar que ambos valores tengan la misma escala
        BigDecimal saldo = saldoActual.setScale(2, RoundingMode.HALF_UP);
        BigDecimal valor = movimientoCreateDto.getValor().setScale(2, RoundingMode.HALF_UP);

        // El valor ya viene con el signo correcto, así que simplemente sumamos
        return saldo.add(valor).setScale(2, RoundingMode.HALF_UP);
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