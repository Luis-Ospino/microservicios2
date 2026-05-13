package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;
import com.example.mscuentasmovimientos.dto.CuentaUpdateDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.exception.CuentaDuplicadaException;
import com.example.mscuentasmovimientos.exception.ResourceNotFoundException;
import com.example.mscuentasmovimientos.mapper.CuentaMapper;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import com.example.mscuentasmovimientos.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;
    private final MovimientoRepository movimientoRepository;

    @Override
    @Transactional
    public CuentaDto createCuenta(CuentaCreateDto cuentaCreateDto) {
        try {
            if (cuentaRepository.existsByNumeroCuenta(cuentaCreateDto.getNumeroCuenta())) {
                throw new CuentaDuplicadaException(cuentaCreateDto.getNumeroCuenta());
            }
            Cuenta cuenta = cuentaMapper.toEntity(cuentaCreateDto);
            Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
            log.info("Cuenta creada: {}", cuentaGuardada.getNumeroCuenta());
            return cuentaMapper.toDto(cuentaGuardada);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Violación de unicidad al crear cuenta: {}", cuentaCreateDto.getNumeroCuenta());
            throw new CuentaDuplicadaException(cuentaCreateDto.getNumeroCuenta());
        }
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
    @Transactional
    public CuentaDto updateCuenta(UUID id, CuentaUpdateDto cuentaUpdateDto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id=" + id));
        // Solo se actualizan tipoCuenta y estado, no numeroCuenta
        cuentaMapper.updateEntityFromDto(cuentaUpdateDto, cuenta);
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        log.info("Cuenta actualizada: {}", cuentaGuardada.getNumeroCuenta());
        return cuentaMapper.toDto(cuentaGuardada);
    }

    @Override
    @Transactional
    public void deleteCuenta(UUID id) {
        if (!cuentaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cuenta no encontrada con id=" + id);
        }
        if (!movimientoRepository.findByCuentaId(id).isEmpty()) {
            throw new IllegalStateException("No se puede eliminar la cuenta porque tiene movimientos asociados");
        }
        cuentaRepository.deleteById(id);
        log.info("Cuenta eliminada: {}", id);
    }
}