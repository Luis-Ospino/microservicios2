package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;

import java.util.List;

public interface CuentaService {

    CuentaDto createCuenta(CuentaCreateDto cuentaCreateDto);

    List<CuentaDto> findAllCuentas();

    CuentaDto findCuentaById(Long id);
}