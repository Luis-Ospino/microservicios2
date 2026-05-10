package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;
import com.example.mscuentasmovimientos.dto.CuentaUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CuentaService {

    CuentaDto createCuenta(CuentaCreateDto cuentaCreateDto);

    List<CuentaDto> findAllCuentas();

    CuentaDto findCuentaById(UUID id);

    List<CuentaDto> findCuentasByClienteId(UUID clienteId);

    CuentaDto updateCuenta(UUID id, CuentaUpdateDto cuentaUpdateDto);

    void deleteCuenta(UUID id);
}