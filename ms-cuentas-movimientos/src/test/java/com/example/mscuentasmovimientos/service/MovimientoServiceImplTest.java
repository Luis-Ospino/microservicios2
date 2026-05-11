package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.MovimientoCreateDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.entity.enums.EstadoCuenta;
import com.example.mscuentasmovimientos.entity.enums.TipoMovimiento;
import com.example.mscuentasmovimientos.exception.SaldoInsuficienteException;
import com.example.mscuentasmovimientos.mapper.MovimientoMapper;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import com.example.mscuentasmovimientos.repository.MovimientoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoMapper movimientoMapper;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Test
    @DisplayName("createMovimiento lanza SaldoInsuficienteException cuando el retiro supera el saldo disponible")
    void createMovimiento_whenRetiroAndBalanceIsInsufficient_thenThrowsSaldoInsuficienteException() {
        // Arrange
        UUID cuentaId = UUID.randomUUID();
        MovimientoCreateDto movimientoCreateDto = MovimientoCreateDto.builder()
                .fecha(LocalDateTime.now())
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(new BigDecimal("-150.00"))
                .cuentaId(cuentaId)
                .build();

        Cuenta cuenta = Cuenta.builder()
                .saldoDisponible(new BigDecimal("100.00"))
                .estado(EstadoCuenta.ACTIVA)
                .build();

        when(cuentaRepository.findByIdForUpdate(cuentaId)).thenReturn(Optional.of(cuenta));

        // Act
        SaldoInsuficienteException exception = assertThrows(
                SaldoInsuficienteException.class,
                () -> movimientoService.createMovimiento(movimientoCreateDto)
        );

        // Assert
        assertEquals("Saldo no disponible", exception.getMessage());
        verify(cuentaRepository).findByIdForUpdate(cuentaId);
        verify(movimientoRepository, never()).save(any());
        verify(cuentaRepository, never()).save(any());
        verifyNoInteractions(movimientoMapper);
    }
}
