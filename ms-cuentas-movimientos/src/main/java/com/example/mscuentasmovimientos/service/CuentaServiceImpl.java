package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.exception.ResourceNotFoundException;
import com.example.mscuentasmovimientos.mapper.CuentaMapper;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    public CuentaDto createCuenta(CuentaCreateDto cuentaCreateDto) {
        Cuenta cuenta = cuentaMapper.toEntity(cuentaCreateDto);
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return cuentaMapper.toDto(cuentaGuardada);
    }

    @Override
    public List<CuentaDto> findAllCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(cuentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDto findCuentaById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id=" + id));
        return cuentaMapper.toDto(cuenta);
    }
}