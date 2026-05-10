package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;
import com.example.mscuentasmovimientos.dto.CuentaUpdateDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.exception.ResourceNotFoundException;
import com.example.mscuentasmovimientos.mapper.CuentaMapper;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    public CuentaDto createCuenta(CuentaCreateDto cuentaCreateDto) {
        if (cuentaRepository.existsByNumeroCuenta(cuentaCreateDto.getNumeroCuenta())) {
            throw new IllegalArgumentException("Ya existe una cuenta con el número: " + cuentaCreateDto.getNumeroCuenta());
        }

        Cuenta cuenta = cuentaMapper.toEntity(cuentaCreateDto);
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return cuentaMapper.toDto(cuentaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDto> findAllCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(cuentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaDto findCuentaById(UUID id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id=" + id));
        return cuentaMapper.toDto(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDto> findCuentasByClienteId(UUID clienteId) {
        return cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(cuentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDto updateCuenta(UUID id, CuentaUpdateDto cuentaUpdateDto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id=" + id));

        cuentaMapper.updateEntityFromDto(cuentaUpdateDto, cuenta);
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return cuentaMapper.toDto(cuentaGuardada);
    }

    @Override
    public void deleteCuenta(UUID id) {
        if (!cuentaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cuenta no encontrada con id=" + id);
        }
        cuentaRepository.deleteById(id);
    }
}