package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.MovimientoCreateDto;
import com.example.mscuentasmovimientos.dto.MovimientoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MovimientoService {

    MovimientoDto createMovimiento(MovimientoCreateDto movimientoCreateDto);

    List<MovimientoDto> findAllMovimientos();

    MovimientoDto findMovimientoById(UUID id);

    List<MovimientoDto> findMovimientosByCuentaId(UUID cuentaId);

    List<MovimientoDto> findMovimientosByCuentaIdAndFechaBetween(UUID cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    void deleteMovimiento(UUID id);
}